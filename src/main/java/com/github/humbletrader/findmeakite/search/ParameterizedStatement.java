package com.github.humbletrader.findmeakite.search;

import java.util.List;

public class ParameterizedStatement {

    private String sqlWithoutParameters;
    private List<Object> paramValues;


    public ParameterizedStatement(String sqlWithoutParameters, List<Object> values){
        this.sqlWithoutParameters = sqlWithoutParameters;
        this.paramValues = values;
    }

    public ParameterizedStatement(ParameterizedStatement initial){
        this.sqlWithoutParameters = initial.sqlWithoutParameters;
        this.paramValues = initial.paramValues;
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
