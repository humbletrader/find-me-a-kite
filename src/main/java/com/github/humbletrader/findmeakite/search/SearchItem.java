package com.github.humbletrader.findmeakite.search;

public class SearchItem {

    private final String link;
    private final String brandNameVersion;
    private double price;
    private final String size;
    private final String condition;

    private boolean visibleToPublic;

    public SearchItem(String brandNameVersion,
                      String link,
                      double price,
                      String size,
                      String condition,
                      boolean isVisibleToPublic) {
        this.brandNameVersion = brandNameVersion;
        this.link = link;
        this.price = price;
        this.size = size;
        this.condition = condition;
        this.visibleToPublic = isVisibleToPublic;
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

    public String getCondition() {
        return condition;
    }

    public boolean isVisibleToPublic(){
        return visibleToPublic;
    }
}
