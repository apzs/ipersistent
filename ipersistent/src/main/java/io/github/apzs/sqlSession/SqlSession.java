package io.github.apzs.sqlSession;

import java.util.List;

public interface SqlSession {

    /**
     * 查询多个结果
     * sqlSession.selectList(); :定位到要执行的sq1语句，从而执行
     * select * from user where username like '% ? %'
     */
    <E> List<E> selectList(String statementId,Object param) throws Exception;

    /**
     * 查询单个结果
     * @param statementId
     * @param param
     * @return
     * @param <T>
     */
    <T> T selectOne(String statementId,Object param) throws Exception;

    /**
     * 关闭资源
     */
    void close();

}
