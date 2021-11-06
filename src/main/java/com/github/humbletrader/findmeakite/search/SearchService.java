package com.github.humbletrader.findmeakite.search;

import com.github.humbletrader.findmeakite.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    public final static int ROWS_PER_PAGE = 20;

    @Autowired
    private SearchRepository searchRepository;

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

    public List<SearchResult> searchByCriteria(SearchCriteria criteria){
        logger.info("searching products by criteria {} ", criteria);
        int startOfPage = criteria.getPage() * ROWS_PER_PAGE;
        int rowsPerPage = ROWS_PER_PAGE;

        logger.info("asking database for page {} from start {} a number of {}", criteria.getPage(), startOfPage, rowsPerPage);

        return searchRepository.pagedSearchByCriteria(criteria, startOfPage, rowsPerPage);
    }

}
