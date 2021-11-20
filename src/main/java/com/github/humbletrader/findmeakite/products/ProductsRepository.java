package com.github.humbletrader.findmeakite.products;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Deprecated
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

    public List<String> findProductSizesByVersionNameBrandAndCategory(String category, String brand, String name, String version) {
        List<String> sizes = jdbcTemplate.query(
                "SELECT distinct size " +
                        "FROM PRODUCTS inner join product_attributes on products.id = product_attributes.product_id " +
                        "where category = ? and brand = ? and name = ? and version = ?",
                (rs, rowCount) -> rs.getString(1),
                category, brand, name, version
        );

        logger.info("products repository found {} sizes ", sizes.size());
        return sizes;
    }
}
