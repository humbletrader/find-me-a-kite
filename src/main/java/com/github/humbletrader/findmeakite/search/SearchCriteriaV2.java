package com.github.humbletrader.findmeakite.search;

import java.util.Map;

public class SearchCriteriaV2 {

    private int page;
    private Map<String, String> criteria;

    public SearchCriteriaV2(int page, Map<String, String> criteria) {
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
        return "SearchCriteriaV2{" +
                "page=" + page +
                ", criteria=" + criteria +
                '}';
    }
}
