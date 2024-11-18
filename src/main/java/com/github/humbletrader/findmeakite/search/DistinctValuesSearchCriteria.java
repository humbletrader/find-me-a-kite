package com.github.humbletrader.findmeakite.search;

import com.github.humbletrader.fmak.query.SearchValAndOp;

import java.util.Map;
import java.util.SequencedSet;

public class DistinctValuesSearchCriteria {

    private final String target;
    private final Map<String, SequencedSet<SearchValAndOp>> criteriaNamesAndValues;

    public DistinctValuesSearchCriteria(String target, Map<String, SequencedSet<SearchValAndOp>> criteria) {
        this.target = target;
        this.criteriaNamesAndValues = criteria;
    }

    public String getTarget(){
        return target;
    }

    public Map<String, SequencedSet<SearchValAndOp>> getCriteria(){
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
