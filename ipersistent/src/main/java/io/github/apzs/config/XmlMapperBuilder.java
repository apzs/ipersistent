package io.github.apzs.config;

import io.github.apzs.pojo.Configuration;
import io.github.apzs.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * 解析映射mapper.xml配置文件 --> mappedStatement --> configuration里面的map集合中
 */
public class XmlMapperBuilder {

    private Configuration configuration;


    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 解析mapper.xml文件增删改查标签
     *     <select id="selectOne" parameter="io.github.apzs.pojo.User" resultType="io.github.apzs.pojo.User">
     *         select * from user where id = #{id} and username = #{username}
     *     </select>
     * @param inputStream
     * @throws DocumentException
     */
    public void parse(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        // 获取根节点
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");

        List<Element> selectList = rootElement.selectNodes("//select");
        for (Element element : selectList) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sql = element.getTextTrim();
            // 封装mappedStatement对象
            MappedStatement mappedStatement = new MappedStatement();
            // StatementId ==> namespace.id
            String statementId = namespace + "." + id;
            mappedStatement.setStatementId(statementId);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setSql(sql);
            // 将封装好的mappedStatement封装到configuration中的map集合中
            configuration.getMappedStatementMap().put(statementId,mappedStatement);
        }

    }
}
