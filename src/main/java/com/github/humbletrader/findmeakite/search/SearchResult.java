package com.github.humbletrader.findmeakite.search;

public class SearchResult {

    private final String link;
    private final String brandNameVersion;
    private double price;
    private final String size;

    public SearchResult(String brandNameVersion, String link, double price, String size) {
        this.brandNameVersion = brandNameVersion;
        this.link = link;
        this.price = price;
        this.size = size;
    }

    public String getLink() {
        return link;
    }

    public String getBrandNameVersion() {
        return brandNameVersion;
    }

    public double getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }
}
