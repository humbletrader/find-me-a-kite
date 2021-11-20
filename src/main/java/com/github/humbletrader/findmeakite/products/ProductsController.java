package com.github.humbletrader.findmeakite.products;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Deprecated
public class ProductsController {

    private static final Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @Autowired
    private ProductsService service;

    @GetMapping(path = "/brandsForCategory")
    public ResponseEntity<Iterable<String>> retrieveBrands(@RequestParam String category) {
        logger.info("retrieving brands for category {} ...", category);
        return new ResponseEntity<>(service.findBrandsForCategory(category), HttpStatus.OK);
    }

    @GetMapping(path = "/namesForCategoryAndBrand")
    public ResponseEntity<Iterable<String>> retrieveProductNamesByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        logger.info("retrieving product names for category {} and brand {} ...", category, brand);
        return new ResponseEntity<>(service.findProductNamesForCategoryAndBrand(category, brand), HttpStatus.OK);
    }

    @GetMapping(path = "/versionsForNameBrandAndCategory")
    public ResponseEntity<Iterable<String>> retrieveProductVersionsByNameCategoryAndBrand(@RequestParam String category, @RequestParam String brand, @RequestParam String name) {
        logger.info("retrieving product versions for category {} brand {} and name {}...", category, brand, name);
        return new ResponseEntity<>(service.findProductVersionsForNameBrandAndCategory(category, brand, name), HttpStatus.OK);
    }

    @GetMapping(path = "/sizeForVersionNameBrandAndCategory")
    public ResponseEntity<Iterable<String>> retrieveProductSizesByVersionNameCategoryAndBrand(@RequestParam String category,
                                                                                              @RequestParam String brand,
                                                                                              @RequestParam String name,
                                                                                              @RequestParam String version) {
        logger.info("retrieving product sizes for category {} brand {} name {} and version {} ...", category, brand, name, version);
        return new ResponseEntity<>(service.findProductVersionsForNameBrandAndCategory(category, brand, name, version), HttpStatus.OK);
    }

}
