package io.github.apzs.sqlSession;

import io.github.apzs.executor.Executor;
import io.github.apzs.pojo.Configuration;
import io.github.apzs.pojo.MappedStatement;

import java.util.List;

public class DefaultSqlSession implements SqlSession{

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration,Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object param) throws Exception {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        // 将查询操作委派给低层的执行器
        List<E> list = executor.query(configuration,mappedStatement,param);
        return list;
    }

    @Override
    public <T> T selectOne(String statementId, Object param) throws Exception {
        // 调用查询所有接口
        List<Object> list = this.selectList(statementId, param);
        if (list == null) {
            return null;
        } else if (list.size() == 1) {
            return (T) list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException("return too many result");
        }
        return null;
    }

    @Override
    public void close() {
        executor.close();
    }
}
