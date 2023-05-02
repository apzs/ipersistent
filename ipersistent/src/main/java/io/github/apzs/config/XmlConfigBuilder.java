package io.github.apzs.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.apzs.io.Resources;
import io.github.apzs.pojo.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * 解析全局配置文件
 */
public class XmlConfigBuilder {

    private Configuration configuration;

    public XmlConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 使用 dom4j + xpath 解析配置文件，封装Configuration对象
     *
     * @param inputStream
     * @return
     */
    public Configuration parse(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        // `//property`是xpath表达式，里面的`//`表示的是rootElement里的所有<property>标签（包括子节点，孙子节点等）
        // 从匹配选择的当前节点选择文档中的节点，而不考虑它们的位置（取子孙节点）
        List<Element> list = rootElement.selectNodes("//property");

        // 封装数据源信息
        Properties properties = new Properties();
        for (Element element : list) {
            // <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name, value);
        }

        // 创建数据源对象
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(properties.getProperty("driverClassName"));
        druidDataSource.setUrl(properties.getProperty("url"));
        druidDataSource.setUsername(properties.getProperty("username"));
        druidDataSource.setPassword(properties.getProperty("password"));

        // 创建好的数据源对象封装到Configuration对象中
        configuration.setDataSource(druidDataSource);

        // ----------解析映射配置文件----
        // 1. 获取映射配置文件的路径
        // 3. 封装到MappedStatement--> configuration里面的map集合中
        // <mapper resource="mapper/UserMapper.xml"></mapper>
        List<Element> mapperList = rootElement.selectNodes("//mapper");
        for (Element element : mapperList) {
            String mapperPath = element.attributeValue("resource");
            // 2. 根据路径进行映射配置文件的加载解析
            InputStream mapperInputStream = Resources.getResourceAsStream(mapperPath);
            // XMLMapperBuilder:专门解析映射配置文件的对象
            XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
            // 解析mapper.xml文件里的增删改查标签，并封装到configuration里
            xmlMapperBuilder.parse(mapperInputStream);
        }

        return configuration;
    }
}
