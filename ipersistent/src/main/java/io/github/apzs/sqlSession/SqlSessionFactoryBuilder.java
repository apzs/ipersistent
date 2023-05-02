package io.github.apzs.sqlSession;

import io.github.apzs.config.XmlConfigBuilder;
import io.github.apzs.pojo.Configuration;
import org.dom4j.DocumentException;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    /**
     * 1.解析配置文件，封装容器对象   2.创建SqlSessionFactory工厂对象
     * @param inputStream
     * @return
     */
    public SqlSessionFactory build(InputStream inputStream) throws DocumentException {
        // 1.解析配置文件，封装容器对象
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse(inputStream);

        // 2.创建SqlSessionFactory工厂
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);

        return defaultSqlSessionFactory;
    }

}
