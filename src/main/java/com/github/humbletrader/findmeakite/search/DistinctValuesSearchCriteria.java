package com.github.humbletrader.findmeakite.search;

import java.util.Map;

public class DistinctValuesSearchCriteria {

    private String target;
    private Map<String, String> criteriaNamesAndValues;

    public DistinctValuesSearchCriteria(String target, Map<String, String> criteria) {
        this.target = target;
        this.criteriaNamesAndValues = criteria;
    }

    public String getTarget(){
        return target;
    }

    public Map<String, String> getCriteria(){
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
