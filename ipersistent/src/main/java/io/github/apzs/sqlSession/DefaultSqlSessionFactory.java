package io.github.apzs.sqlSession;

import io.github.apzs.executor.Executor;
import io.github.apzs.executor.SimpleExecutor;
import io.github.apzs.pojo.Configuration;

public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        // 创建执行器
        Executor simpleExecutor = new SimpleExecutor();
        // 产生SqlSession对象
        SqlSession sqlSession = new DefaultSqlSession(configuration,simpleExecutor);

        return sqlSession;
    }
}
