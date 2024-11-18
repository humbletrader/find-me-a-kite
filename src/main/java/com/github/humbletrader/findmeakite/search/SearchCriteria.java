package com.github.humbletrader.findmeakite.search;

import com.github.humbletrader.fmak.query.SearchValAndOp;

import java.util.Map;
import java.util.SequencedSet;

public class SearchCriteria {

    private final int page;
    private final Map<String, SequencedSet<SearchValAndOp>> criteria;
    private final String supporterToken;

    public SearchCriteria(int page,
                          Map<String, SequencedSet<SearchValAndOp>> criteria,
                          String token) {
        this.page = page;
        this.criteria = criteria;
        this.supporterToken = token;
    }

    public int getPage() {
        return page;
    }

    public Map<String, SequencedSet<SearchValAndOp>> getCriteria() {
        return criteria;
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
