package com.github.humbletrader.findmeakite.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Deprecated
public class ProductsService {

    @Autowired
    private ProductsRepository repository;

    public List<String> findBrandsForCategory(String category){
        return repository.findBrandsByCategory(category);
    }

    public List<String> findProductNamesForCategoryAndBrand(String category, String brand){
        return repository.findProductNamesByCategoryAndBrand(category, brand);
    }

    public List<String> findProductVersionsForNameBrandAndCategory(String category, String brand, String name){
        return repository.findProductVersionsByNameBrandAndCategory(category, brand, name);
    }

    public List<String> findProductVersionsForNameBrandAndCategory(String category, String brand, String name, String version){
        return repository.findProductSizesByVersionNameBrandAndCategory(category, brand, name, version);
    }

}
