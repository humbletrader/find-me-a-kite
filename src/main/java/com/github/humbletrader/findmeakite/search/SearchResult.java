package com.github.humbletrader.findmeakite.search;

public class SearchResult {

    private String productName;
    private String link;
    private String brandNameVersion;

    public SearchResult(String productName, String link, String brandNameVersion) {
        this.productName = productName;
        this.link = link;
        this.brandNameVersion = brandNameVersion;
    }

    public String getProductName() {
        return productName;
    }

    public String getLink() {
        return link;
    }

    public String getBrandNameVersion() {
        return brandNameVersion;
    }
}
