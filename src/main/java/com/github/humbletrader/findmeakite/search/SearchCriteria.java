package com.github.humbletrader.findmeakite.search;

import java.util.Map;

public class SearchCriteria {

    private int page;
    private Map<String, String> criteria;

    public SearchCriteria(int page, Map<String, String> criteria) {
        this.page = page;
        this.criteria = criteria;
    }

    public int getPage() {
        return page;
    }

    public Map<String, String> getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "page=" + page +
                ", criteria=" + criteria +
                '}';
    }
}
