package com.github.humbletrader.findmeakite.search;

public class SearchResult {

    private String productName;
    private String link;

    public SearchResult(String productName, String link) {
        this.productName = productName;
        this.link = link;
    }

    public String getProductName() {
        return productName;
    }

    public String getLink() {
        return link;
    }
}
