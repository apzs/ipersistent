package io.github.apzs.config;

import io.github.apzs.utils.ParameterMapping;

import java.util.List;

public class BoundSql {

    private String finallySql;

    private List<ParameterMapping> parameterMappingList;

    public BoundSql(String finallySql, List<ParameterMapping> parameterMappingList) {
        this.finallySql = finallySql;
        this.parameterMappingList = parameterMappingList;
    }

    public String getFinallySql() {
        return finallySql;
    }

    public void setFinallySql(String finallySql) {
        this.finallySql = finallySql;
    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }
}
