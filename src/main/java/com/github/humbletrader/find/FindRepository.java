package com.github.humbletrader.find;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

    public List<Product> findProductsByName(String name){
        return jdbcTemplate.query(
                "SELECT brand, name, version , link, category FROM PRODUCTS",
                (resultSet, rowCount) ->
                        new Product(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5)));
    }

}
