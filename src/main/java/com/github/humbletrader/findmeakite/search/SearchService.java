package com.github.humbletrader.findmeakite.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public List<SearchResult> searchByCriteria(SearchCriteria criteria){
        logger.info("searching products by criteria {} ", criteria);
        int startOfPage = criteria.getPage() * ROWS_PER_PAGE;
        int rowsPerPage = ROWS_PER_PAGE;

        logger.info("asking database for page {} from start {} a number of {}", criteria.getPage(), startOfPage, rowsPerPage);

        return searchRepository.pagedSearchByCriteria(criteria, startOfPage, rowsPerPage);
    }

    public List<String> searchDistinctValuesByCriteria(DistinctValuesSearchCriteria criteria){
        StringBuilder selectString = new StringBuilder();
        selectString.append("select distinct p.").append(criteria.getTarget())
                .append(" from products p")
                .append(" where");
        for (Map.Entry<String, String> criteriaEntry : criteria.getCriteria().entrySet()) {
            selectString.append("p.").append(criteriaEntry.getKey()).append("='").append(criteriaEntry.getValue()).append("'");
        }

        return searchRepository.searchDistinctValues(selectString.toString());
    }

}
