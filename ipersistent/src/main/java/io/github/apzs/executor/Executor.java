package io.github.apzs.executor;

import io.github.apzs.pojo.Configuration;
import io.github.apzs.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object param) throws Exception;

    void close();
}
