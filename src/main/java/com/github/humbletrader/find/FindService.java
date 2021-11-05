package com.github.humbletrader.find;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FindService {

    private static final Logger logger = LoggerFactory.getLogger(FindService.class);

    @Autowired
    private FindRepository findRepository;

    public List<String> findCategories(){
        logger.info("servicing supported categories ...");
        return Arrays.asList("KITE", "TWINTIPS", "BARS", "WETSUITS");
    }

    public List<Product> findBrandsInCategory(String category){
        logger.info("servicing brands in category {} ...", category);
        List<Product> result = new ArrayList<>();
        result.add(new Product("Cabrinha Switchblade 2020", "Cabrinha", "switchblade", "http:url", "kites"));
        return result;
    }

    public List<Product> findByName(String brand, String name){
        logger.info("searching products by name {} and brand {}", name, brand);
        return findRepository.findProductsByName(name);
    }

    public List<Product> findByNameAndSize(String brand, String name, String size) {
        List<Product> result = new ArrayList<>();
        return result;
    }

    public List<Product> findByNameAndColor(String brand, String name, String color){
        List<Product> result = new ArrayList<>();
        return result;
    }

}
