package com.github.humbletrader.find;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FindService {

    private static final Logger logger = LoggerFactory.getLogger(FindService.class);

    public List<String> findCategories(){
        logger.info("servicing supported categories ...");
        return Arrays.asList("KITE", "TWINTIPS", "BARS", "WETSUITS");
    }

    public List<Product> findBrandsInCategory(String category){
        logger.info("servicing brands in category {} ...", category);
        List<Product> result = new ArrayList<>();
        result.add(new Product("Switchblade", "Cabrinha"));
        result.add(new Product("Lithium", "Airush"));
        result.add(new Product("Evo", "Duotone"));
        return result;
    }

    public List<Product> findByName(String brand, String name){
        List<Product> result = new ArrayList<>();
        result.add(new Product("Evo", "Duotone"));
        return result;
    }

    public List<Product> findByNameAndSize(String brand, String name, String size) {
        List<Product> result = new ArrayList<>();
        result.add(new Product("Switchblade", "Cabrinha"));
        return result;
    }

    public List<Product> findByNameAndColor(String brand, String name, String color){
        List<Product> result = new ArrayList<>();
        result.add(new Product("Lithium", "Airush"));
        return result;
    }

}
