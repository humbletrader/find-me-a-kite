package com.github.humbletrader.findmeakite.products;

import com.github.humbletrader.findmeakite.search.SearchService;
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

        logger.info(" brands repository found {}", brands.size());

        return brands;
    }
}
