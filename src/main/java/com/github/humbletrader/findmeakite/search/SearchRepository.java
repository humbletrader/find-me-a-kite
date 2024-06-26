package com.github.humbletrader.findmeakite.search;

import com.github.humbletrader.fmak.query.ParameterizedStatement;
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

    public List<String> searchDistinctValues(ParameterizedStatement searchStatement){
        logger.info("searching for distinct values with : {} ", searchStatement);
        return jdbcTemplate.query(searchStatement.getSqlWithoutParameters(),
                (rs, rowCount) -> rs.getString(1),
                searchStatement.getParamValues().toArray()
        );
    }

    public List<SearchResultItem> pagedSearchByCriteria(ParameterizedStatement searchStatement){
        logger.info("searching for products with : {} ", searchStatement);
        return jdbcTemplate.query(
                searchStatement.getSqlWithoutParameters(),
                //"brand_name_version", "link", "price", "size", "condition", "visible_to_public"
                (rs, rowCount) -> new SearchResultItem(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getBoolean(6)),
                searchStatement.getParamValues().toArray()
        );
    }

}
