package com.github.humbletrader.findmeakite.search;

import java.util.Map;

public class SearchCriteria {

    private int page;
    private Map<String, SearchValAndOp> criteria;
    private String supporterToken;

    public SearchCriteria(int page,
                          Map<String, SearchValAndOp> criteria,
                          String token) {
        this.page = page;
        this.criteria = criteria;
        this.supporterToken = token;
    }

    public int getPage() {
        return page;
    }

    public Map<String, SearchValAndOp> getCriteria() {
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
