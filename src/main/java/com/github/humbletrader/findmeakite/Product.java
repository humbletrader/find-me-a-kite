package com.github.humbletrader.findmeakite;

public class Product {

    @Deprecated
    private String brandAndNameAndVersion;
    private String brand;
    private String name;
    private String version;
    private String link;
    private String category;

    private boolean visibleToPublic;

    @Deprecated
    public Product(String brandAndNameAndVersion,
                   String brand,
                   String name,
                   String version,
                   String link,
                   String category){
        this.brandAndNameAndVersion = brandAndNameAndVersion;
        this.brand = brand;
        this.name = name;
        this.version = version;
        this.link = link;
        this.category = category;
        this.visibleToPublic = false;
    }

    public Product(String brand,
                   String name,
                   String version,
                   String link,
                   String category,
                   boolean visibleToPublic){
        this.brandAndNameAndVersion = brand+" "+name+" "+version;
        this.brand = brand;
        this.name = name;
        this.version = version;
        this.link = link;
        this.category = category;
        this.visibleToPublic = visibleToPublic;
    }

    public String getBrandAndNameAndVersion() {
        return brandAndNameAndVersion;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getLink(){
        return link;
    }

    public String getCategory() {
        return category;
    }

    public boolean isVisibleToPublic(){
        return visibleToPublic;
    }

    @Override
    public String toString() {
        return "Product{" +
                "brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", isVisibleToPublic=" + visibleToPublic +
                '}';
    }
}
