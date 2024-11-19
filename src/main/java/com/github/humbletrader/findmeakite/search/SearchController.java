package com.github.humbletrader.findmeakite.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/")
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    private final SearchService searchService;

    public SearchController(SearchService findService) {
        this.searchService = findService;
    }

    @GetMapping(path = "/categories", consumes = {"application/json;charset=UTF-8"})
    public ResponseEntity<Iterable<String>> retrieveCategories() {
        logger.info("retrieving supported categories ...");
        return new ResponseEntity<>(searchService.findCategories(), HttpStatus.OK);
    }

    @PostMapping(path = "/searchDistinctValues", consumes = {"application/json;charset=UTF-8"})
    public ResponseEntity<Iterable<String>> retrieveDistinctValues(@RequestBody DistinctValuesSearchCriteria criteria){
        logger.info("searching distinct values for {}", data);
        return new ResponseEntity<>(searchService.searchDistinctValuesByCriteria(data), HttpStatus.OK);
    }

    @PostMapping(path = "/search", consumes = {"application/json;charset=UTF-8"})
    public ResponseEntity<SearchResultPage> retrieveProduct(@RequestBody SearchCriteria criteria) {
        logger.info("searching products by {} ", criteria);
        return new ResponseEntity<>(searchService.searchByCriteria(criteria), HttpStatus.OK);
    }
}