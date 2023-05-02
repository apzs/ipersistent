package io.github.apzs.test;

import io.github.apzs.io.Resources;
import io.github.apzs.pojo.User;
import io.github.apzs.sqlSession.SqlSession;
import io.github.apzs.sqlSession.SqlSessionFactory;
import io.github.apzs.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.io.InputStream;

public class IPersistentTest {

    /**
     * 传统方式(不使用mapper代理)测试
     **/
    @Test
    public void test1() throws Exception {
        // 1.根据配置文件的路径，加载成字节输入流，存到内存中注意:配置文件还未解析
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        System.out.println(resourceAsStream);

        // 2. 解析配置文件，封装Configuration对象；创建SqlSessionFactory工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        // 生产sqlSession创建了执行器对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 调用sqlSession方法
        User user = new User();
        user.setId(1);
        user.setUsername("tom");
        User resultUser = sqlSession.selectOne("user.selectOne", user);

        System.out.println(resultUser);

        // 释放资源
        sqlSession.close();
    }

}
