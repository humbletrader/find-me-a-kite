package com.github.humbletrader.findmeakite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/")
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    private SearchService findService;

    public SearchController(SearchService findService) {
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

    @PostMapping(path = "/search")
    public ResponseEntity<Iterable<SearchResult>> retrieveProduct(@RequestBody SearchCriteria criteria) {
        logger.info("searching products by {} ", criteria);
        return new ResponseEntity<>(findService.searchByCriteria(criteria), HttpStatus.OK);
    }
}