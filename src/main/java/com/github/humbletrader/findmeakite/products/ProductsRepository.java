package com.github.humbletrader.findmeakite.products;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductsRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductsRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> findBrandsByCategory(String category){
        return jdbcTemplate.query(
                "SELECT distinct brand FROM PRODUCTS where category = ?",
                (rs, rowCount) -> rs.getString(1),
                category
        );
    }
}
