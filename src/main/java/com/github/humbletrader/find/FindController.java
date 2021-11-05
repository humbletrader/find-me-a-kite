package com.github.humbletrader.find;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/")
public class FindController {

    private static final Logger logger = LoggerFactory.getLogger(FindController.class);

    private FindService findService;

    public FindController(FindService findService) {
        this.findService = findService;
    }

    @GetMapping(path = "/categories")
    public ResponseEntity<Iterable<String>> retrieveCategories() {
        logger.info("retrieving supported categories ...");
        return new ResponseEntity<>(findService.findCategories(), HttpStatus.OK);
    }

    @GetMapping(path = "/brandsInCategory")
    public ResponseEntity<Iterable<Product>> retrieveBrands(@RequestParam String category) {
        logger.info("retrieving brands for category {} ...", category);
        return new ResponseEntity<>(findService.findBrandsInCategory(category), HttpStatus.OK);
    }

    @GetMapping(path = "/productByName")
    public ResponseEntity<Iterable<Product>> retrieveProduct() {
        logger.info("retrieving product by name {} ...");
        return new ResponseEntity<>(findService.findByName("brand", "name"), HttpStatus.OK);
    }
}