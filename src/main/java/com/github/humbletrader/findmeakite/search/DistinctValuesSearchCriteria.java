package com.github.humbletrader.findmeakite.search;

import com.github.humbletrader.fmak.query.SearchValAndOp;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SequencedSet;
import java.util.stream.Collectors;

public class DistinctValuesSearchCriteria {

    private final String target;
    private final Map<String, List<SearchValAndOp>> criteriaNamesAndValues;


//    {"target":"year",
//            "criteria":
//                {
//                    "category":[{"value":"KITES","op":"eq"}],
//                    "country":[{"value":"EU","op":"eq"}]
//                }
//    }
    public DistinctValuesSearchCriteria(String target, Map<String, List<SearchValAndOp>> criteria) {
        this.target = target;
        this.criteriaNamesAndValues = criteria;
    }

    public String getTarget(){
        return target;
    }

    public Map<String, SequencedSet<SearchValAndOp>> getCriteria(){
        return criteriaNamesAndValues.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new LinkedHashSet<>(e.getValue())));
    }

    @Override
    public String toString() {
        return "DistinctValuesSearchCriteria{" +
                "target='" + target + '\'' +
                ", criteriaNamesAndValues=" + criteriaNamesAndValues +
                '}';
    }
}
