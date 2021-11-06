package com.github.humbletrader.findmeakite.search;

public class SearchCriteria {

    private String category;
    private String brand;
    private String productName;
    private String productVersion;
    private String size;
    private String color;
    private int page;

    public SearchCriteria(String category, String brand, String productName, String productVersion, String size, String color, int page) {
        this.category = category;
        this.brand = brand;
        this.productName = productName;
        this.productVersion = productVersion;
        this.size = size;
        this.color = color;
        this.page = page;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public int getPage() {
        return page;
    }
}
