package com.github.humbletrader.findmeakite;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SearchRepository {

    private final JdbcTemplate jdbcTemplate;

    public SearchRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SearchResult> searchByCriteria(SearchCriteria criteria){
        return jdbcTemplate.query(
                "SELECT name, link FROM PRODUCTS",
                (rs, rowCount) ->
                        new SearchResult(rs.getString(1), rs.getString(2))
        );
    }

}
