package com.github.humbletrader.findmeakite.search;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SearchRepository {

    private final JdbcTemplate jdbcTemplate;

    public SearchRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SearchResult> pagedSearchByCriteria(SearchCriteria criteria, int startOfPage, int resultsPerPage){
        return jdbcTemplate.query(
                "SELECT name, link FROM PRODUCTS order by id LIMIT ? OFFSET ?",
                (rs, rowCount) -> new SearchResult(rs.getString(1), rs.getString(2)),
                resultsPerPage, startOfPage
        );
    }

}
