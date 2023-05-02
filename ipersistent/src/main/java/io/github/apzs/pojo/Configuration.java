package io.github.apzs.pojo;

import io.github.apzs.pojo.MappedStatement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置类，存放sqlMapConfig.xml核心配置文件解析出来的内容
 * 通过 {@link io.github.apzs.config.XmlConfigBuilder} 进行解析，并将解析的内容封装到本类
 */
public class Configuration  {

    // 数据源对象
    private DataSource dataSource;

    // 声明mapper  key为statementId也就是namespace.id  value为MappedStatement:封装好的mappedStatement对象
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }
}
