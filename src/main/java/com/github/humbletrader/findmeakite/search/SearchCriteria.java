package com.github.humbletrader.findmeakite.search;

import com.github.humbletrader.fmak.query.SearchValAndOp;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SequencedSet;
import java.util.stream.Collectors;

public class SearchCriteria {

    private final int page;
    private final Map<String, List<SearchValAndOp>> criteria;
    private final String supporterToken;


    //{ "page":0,
    //  "supporterToken":"none",
    //  "criteria":{
    //      "category":[
    //          {"value":"KITES","op":"eq"}
    //       ],
    //       "country":[
    //          {"value":"EU","op":"eq"}
    //        ]
    //   }
    //}
    public SearchCriteria(int page,
                          Map<String, List<SearchValAndOp>> criteria,
                          String token) {
        this.page = page;
        this.criteria = criteria;
        this.supporterToken = token;
    }

    public int getPage() {
        return page;
    }

    public Map<String, SequencedSet<SearchValAndOp>> getCriteria() {
        return criteria.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new LinkedHashSet<>(e.getValue())));
    }

    public String getSupporterToken(){
        return supporterToken;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "page=" + page +
                ", criteria=" + criteria +
                ", supporter= "+supporterToken +
                '}';
    }
}
