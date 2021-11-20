package com.github.humbletrader.findmeakite.search;

import java.util.Arrays;

public class SearchStatement {

    private String sqlWithoutParameters;
    private Object[] paramValues;


    public SearchStatement(String sqlWithoutParameters, Object[] values){
        this.sqlWithoutParameters = sqlWithoutParameters;
        this.paramValues = values;
    }

    public String getSqlWithoutParameters() {
        return sqlWithoutParameters;
    }

    public Object[] getParamValues() {
        return paramValues;
    }

    @Override
    public String toString() {
        return "SearchStatement{" +
                "sqlWithoutParameters='" + sqlWithoutParameters + '\'' +
                ", paramValues=" + Arrays.toString(paramValues) +
                '}';
    }
}
