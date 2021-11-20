package com.github.humbletrader.findmeakite.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SearchRepository {

    private static final Logger logger = LoggerFactory.getLogger(SearchRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public SearchRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> searchDistinctValues(SearchStatement searchStatement){
        logger.info("searching for distinct values with : {} ", searchStatement);
        return jdbcTemplate.query(searchStatement.getSqlWithoutParameters(),
                (rs, rowCount) -> rs.getString(1),
                searchStatement.getParamValues()
        );
    }

    public List<SearchResult> pagedSearchByCriteriaV2(SearchStatement searchStatement){
        logger.info("searching for products with : {} ", searchStatement);
        return jdbcTemplate.query(
                searchStatement.getSqlWithoutParameters(),
                (rs, rowCount) -> new SearchResult(rs.getString(1), rs.getString(2), rs.getString(3)),
                searchStatement.getParamValues()
        );
    }

}
