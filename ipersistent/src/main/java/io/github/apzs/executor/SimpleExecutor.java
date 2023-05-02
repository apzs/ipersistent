package io.github.apzs.executor;

import io.github.apzs.config.BoundSql;
import io.github.apzs.pojo.Configuration;
import io.github.apzs.pojo.MappedStatement;
import io.github.apzs.utils.GenericTokenParser;
import io.github.apzs.utils.ParameterMapping;
import io.github.apzs.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object param) throws Exception {
        // 加载数据库驱动，获取数据库连接
        connection = configuration.getDataSource().getConnection();
        // 获取preparedStatement预编译对象
        String sql = mappedStatement.getSql();
        /*
         将     select * from user where id = #{id} and username = #{username}
         替换为  select * from user where id =   ?   and username =      ?
         并将   #{}里面的值保存起来
         */

        BoundSql boundSql = getBoundSql(sql);
        String finallySql = boundSql.getFinallySql();
        preparedStatement = connection.prepareStatement(finallySql);
        // 设置参数；这里是 io.github.apzs.pojo.User
        String parameterType = mappedStatement.getParameterType();
        Class<?> parameterTypeClass = Class.forName(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        // 遍历parameterMappingList
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            // 获取内容（如果是上面的例子，则content 就是 id 或 username）
            String paramName = parameterMapping.getContent();
            // 通过反射，第一次遍历获取到User参数对象的`#{id}`里面的id字段
            // 通过反射，第二次遍历获取到User参数对象的`#{username}`里面的username字段
            Field declaredField = parameterTypeClass.getDeclaredField(paramName);
            // 如果是私有的字段，则暴力访问
            declaredField.setAccessible(true);
            // 获取param对象的这个字段的值
            Object value = declaredField.get(param);
            // setObject的parameterIndex是从1开始的
            preparedStatement.setObject(i+1,value);
        }
        // 执行sql，发起查询
        resultSet = preparedStatement.executeQuery();
        // 处理返回结果集
        ArrayList<E> resultObjectList = new ArrayList<>();
        // 处理返回结果集
        while (resultSet.next()) {
            // 元数据信息；包含了 字段名 和 字段的值
            ResultSetMetaData metaData = resultSet.getMetaData();
            // 返回类型的值 这里时io.github.apzs.pojo.User，和参数的类型一样
            String resultType = mappedStatement.getResultType();
            Class<?> resultTypeClass = Class.forName(resultType);
            // 使用无参构造创建对象
            Object resultObject = resultTypeClass.getConstructor().newInstance();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                // 获取数据库的列名（索引从1开始，示例中是列名是 id 或 username 或 password，当然这里的数据库列名对应的也是实体类的字段名）
                String columnName = metaData.getColumnName(i);
                // 获取指定列名的值，也就是要封装的实体类的字段的值
                Object columnValue = resultSet.getObject(columnName);
                // java.beans包下的PropertyDescriptor，属性描述器，通过API方法获取某个属性的读写方法
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                // 得到columnName字段的写方法
                Method writeMethod = propertyDescriptor.getWriteMethod();
                // 参数1：实例对象，参数2：要设置的值
                writeMethod.invoke(resultObject,columnValue);
            }
            resultObjectList.add((E) resultObject);
        }
        return resultObjectList;
    }

    private BoundSql getBoundSql(String sql) {
        // 创建标记处理器：配合标记解析器完成标记的处理解析工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        // 创建标记解析器
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{","}",parameterMappingTokenHandler);
        // 将#{}替换为?；解析过程中将#{}里面的值保存到ParameterMapping里
        String finallySql = genericTokenParser.parse(sql);
        // 获取#{}里面的值的集合
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(finallySql,parameterMappings);
        return boundSql;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
