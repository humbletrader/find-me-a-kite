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

    private SearchService searchService;

    public SearchController(SearchService findService) {
        this.searchService = findService;
    }

    @GetMapping(path = "/categories")
    public ResponseEntity<Iterable<String>> retrieveCategories() {
        logger.info("retrieving supported categories ...");
        return new ResponseEntity<>(searchService.findCategories(), HttpStatus.OK);
    }

    @PostMapping(path = "/searchDistinctValues")
    public ResponseEntity<Iterable<String>> retrieveDistinctValues(@RequestBody DistinctValuesSearchCriteria data){
        logger.info("searching distinct values for {}", data);
        return new ResponseEntity<>(searchService.searchDistinctValuesByCriteria(data), HttpStatus.OK);
    }

    @PostMapping(path = "/search")
    public ResponseEntity<SearchResultPage> retrieveProduct(@RequestBody SearchCriteriaV2 criteria) {
        logger.info("searching products by {} ", criteria);
        return new ResponseEntity<>(searchService.searchByCriteria(criteria), HttpStatus.OK);
    }
}