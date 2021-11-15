package com.github.humbletrader.findmeakite.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository repository;

    public List<String> findBrandsForCategory(String category){
        return repository.findBrandsByCategory(category);
    }

}
