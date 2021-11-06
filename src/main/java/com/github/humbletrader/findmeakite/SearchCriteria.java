package com.github.humbletrader.findmeakite;

public class SearchCriteria {

    private String category;
    private String brand;
    private String productName;
    private String productVersion;
    private String size;
    private String color;

    public SearchCriteria(String category, String brand, String productName, String productVersion, String size, String color) {
        this.category = category;
        this.brand = brand;
        this.productName = productName;
        this.productVersion = productVersion;
        this.size = size;
        this.color = color;
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
}
