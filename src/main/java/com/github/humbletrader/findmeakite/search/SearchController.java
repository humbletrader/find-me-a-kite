package com.github.humbletrader.findmeakite.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


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

    @PostMapping(path = "/search")
    public ResponseEntity<Iterable<SearchResult>> retrieveProduct(@RequestBody SearchCriteria criteria) {
        logger.info("searching products by {} ", criteria);
        return new ResponseEntity<>(findService.searchByCriteria(criteria), HttpStatus.OK);
    }

    @PostMapping(path = "/searchDistinctValues")
    public ResponseEntity<Iterable<String>> retrieveDistinctValues(@RequestBody DistinctValuesSearchCriteria data){
        logger.info("searching distinct values for {}", data);
        return new ResponseEntity<>(Arrays.asList("val1", "val2", "val3"), HttpStatus.OK);
    }
}