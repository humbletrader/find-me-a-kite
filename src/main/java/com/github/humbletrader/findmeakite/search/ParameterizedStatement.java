package com.github.humbletrader.findmeakite.search;

import java.util.Arrays;
import java.util.List;

public class ParameterizedStatement {

    private String sqlWithoutParameters;
    private List<Object> paramValues;


    public ParameterizedStatement(String sqlWithoutParameters, List<Object> values){
        this.sqlWithoutParameters = sqlWithoutParameters;
        this.paramValues = values;
    }

    public String getSqlWithoutParameters() {
        return sqlWithoutParameters;
    }

    public List<Object> getParamValues() {
        return paramValues;
    }

    @Override
    public String toString() {
        return "ParameterizedStatement{" +
                "sqlWithoutParameters='" + sqlWithoutParameters + '\'' +
                ", paramValues=" + paramValues +
                '}';
    }
}
