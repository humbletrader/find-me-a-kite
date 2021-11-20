package com.github.humbletrader.findmeakite.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        SearchStatement searchStatement = buildSqlWithFilters(criteria.getCriteria(), OptionalInt.empty(), "distinct p." + criteria.getTarget());
        return searchRepository.searchDistinctValues(searchStatement);
    }

    public List<SearchResult> searchByCriteriaV2(SearchCriteriaV2 criteria){
        logger.info("searching products by criteria {} ", criteria);

        SearchStatement sql = buildSqlWithFilters(criteria.getCriteria(), OptionalInt.of(criteria.getPage()), "name", "link", "brand_name_version");

        return searchRepository.pagedSearchByCriteriaV2(sql);
    }

    private SearchStatement buildSqlWithFilters(Map<String, String> criteria, OptionalInt page, String... columns){
        StringBuilder selectString = new StringBuilder("select ");
        List<Object> valuesForParameters = new ArrayList<>();

        for (int i = 0; i < columns.length - 1; i++) {
            selectString.append(columns[i]).append(",");
        }
        //we write the last one without comma at the end
        selectString.append(columns[columns.length - 1]);
        selectString.append(" from products p")
                .append(" where ")
                .append(" p.category = ?");
        valuesForParameters.add(criteria.get("category"));

        for (Map.Entry<String, String> criteriaEntry : criteria.entrySet()) {
            if(!criteriaEntry.getKey().equals("category")){
                selectString.append(" and p.").append(criteriaEntry.getKey()).append("= ? ");
                valuesForParameters.add(criteriaEntry.getValue());
            }
        }

        if(page.isPresent()){
            int startOfPage = page.getAsInt() * ROWS_PER_PAGE;
            int rowsPerPage = ROWS_PER_PAGE;

            selectString.append("order by id LIMIT ? OFFSET ?");
            valuesForParameters.add(rowsPerPage);
            valuesForParameters.add(startOfPage);

        }
        return new SearchStatement(selectString.toString(), valuesForParameters.toArray());
    }

}
