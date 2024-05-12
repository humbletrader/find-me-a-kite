package com.github.humbletrader.findmeakite.search;

import java.util.List;

public class SearchResultPage {

    private final int currentPage;
    private final boolean hasNext;
    private final List<SearchResultItem> items;

    public SearchResultPage(int currentPage, List<SearchResultItem> items, boolean hasNext) {
        this.currentPage = currentPage;
        this.hasNext = hasNext;
        this.items = items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isNextPage() {
        return hasNext;
    }

    public List<SearchResultItem> getItems() {
        return items;
    }
}
