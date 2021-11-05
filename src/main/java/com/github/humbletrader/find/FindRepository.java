package com.github.humbletrader.find;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FindRepository {

    private final JdbcTemplate jdbcTemplate;

    public FindRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> listBrandsInCategory(){
        jdbcTemplate.queryForObject(
                "SELECT * FROM PRODUCTS",
                (rs, rowId) -> new Product(rs.getString("name"), rs.getString("brand")));
    }

}