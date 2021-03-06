package com.github.humbletrader.findmeakite;

public class Product {

    @Deprecated
    private String brandAndNameAndVersion;
    private String brand;
    private String name;
    private String version;
    private String link;
    private String category;

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
    }

    public Product(String brand,
                   String name,
                   String version,
                   String link,
                   String category){
        this.brandAndNameAndVersion = brand+" "+name+" "+version;
        this.brand = brand;
        this.name = name;
        this.version = version;
        this.link = link;
        this.category = category;
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

    @Override
    public String toString() {
        return "Product{" +
                "brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
