package com.github.humbletrader.find;

public class Product {

    @Deprecated
    private String brandAndNameAndVersion;
    private String brand;
    private String name;
    private String version;
    private String link;
    private String category;

    public Product(){}

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

    public void setBrandAndNameAndVersion(String brandAndNameAndVersion) {
        this.brandAndNameAndVersion = brandAndNameAndVersion;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCategory(String category) {
        this.category = category;
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
