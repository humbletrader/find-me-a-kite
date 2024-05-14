package com.github.humbletrader.findmeakite.search;

import java.util.Map;

public class DistinctValuesSearchCriteria {

    private final String target;
    private final Map<String, SearchValAndOp> criteriaNamesAndValues;

    public DistinctValuesSearchCriteria(String target, Map<String, SearchValAndOp> criteria) {
        this.target = target;
        this.criteriaNamesAndValues = criteria;
    }

    public String getTarget(){
        return target;
    }

    public Map<String, SearchValAndOp> getCriteria(){
        return criteriaNamesAndValues;
    }

    @Override
    public String toString() {
        return "DistinctValuesSearchCriteria{" +
                "target='" + target + '\'' +
                ", criteriaNamesAndValues=" + criteriaNamesAndValues +
                '}';
    }
}
