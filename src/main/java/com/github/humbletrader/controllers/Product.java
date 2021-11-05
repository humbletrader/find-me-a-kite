package com.github.humbletrader.controllers;

public class Product {

    private String name;
    private String brand;

    public Product(String name, String brand) {
        this.name = name;
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }
}
