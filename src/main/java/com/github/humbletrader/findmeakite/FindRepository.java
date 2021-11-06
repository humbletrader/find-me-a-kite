package com.github.humbletrader.findmeakite;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FindRepository {

    private final JdbcTemplate jdbcTemplate;

    public FindRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findProductsByName(String name){
        return jdbcTemplate.query(
                "SELECT brand, name, version , link, category FROM PRODUCTS",
                (rs, rowCount) ->
                        new Product(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5))
        );
    }

}
