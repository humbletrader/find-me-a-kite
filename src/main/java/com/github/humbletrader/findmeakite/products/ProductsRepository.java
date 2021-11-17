package com.github.humbletrader.findmeakite.products;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductsRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProductsRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public ProductsRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> findBrandsByCategory(String category){
        List<String> brands = jdbcTemplate.query(
                "SELECT distinct brand FROM PRODUCTS where category = ?",
                (rs, rowCount) -> rs.getString(1),
                category
        );

        logger.info("products repository found {} brands", brands.size());

        return brands;
    }

    public List<String> findProductNamesByCategoryAndBrand(String category, String brand){
        List<String> names = jdbcTemplate.query(
                "SELECT distinct name FROM PRODUCTS where category = ? and brand = ?",
                (rs, rowCount) -> rs.getString(1),
                category, brand
        );

        logger.info("product repository found {} names", names.size());

        return names;
    }

    public List<String> findProductVersionsByNameBrandAndCategory(String category, String brand, String name){
        List<String> versions = jdbcTemplate.query(
                "SELECT distinct version FROM PRODUCTS where category = ? and brand = ? and name = ?",
                (rs, rowCount) -> rs.getString(1),
                category, brand, name
        );

        logger.info("products repository found {} versions", versions.size());
        return versions;
    }
}
