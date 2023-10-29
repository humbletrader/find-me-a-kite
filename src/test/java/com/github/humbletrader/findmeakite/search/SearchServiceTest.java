package com.github.humbletrader.findmeakite.search;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SearchServiceTest {

    private SearchService underTest = new SearchService("token1,token2");

    @Test
    public void sqlBuildForTwoParametersNonProductAttributes(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");
        filters.put("country", "EU");
        filters.put("name", "cabrinha");

        ParameterizedStatement result = underTest.buildDistinctValuesSql(filters,   "name");
        assertEquals(
                "select distinct p.name "+
                        "from products p "+
                        "inner join shops s on s.id = p.shop_id "+
                        "where p.category = ? "+
                        "and s.country = ? "+
                        "and p.name = ? "+
                        "order by name",
                result.getSqlWithoutParameters()
        );
        assertEquals(Arrays.asList("KITES", "EU", "cabrinha"), result.getParamValues());
    }

    @Test
    public void sqlShouldContainAJoinWhenSizeIsInFilter(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");
        filters.put("country", "EU");
        filters.put("name", "cabrinha");
        filters.put("size", "10");

        ParameterizedStatement result = underTest.buildDistinctValuesSql(filters, "name");
        assertEquals(
                "select distinct p.name " +
                        "from products p "+
                        "inner join shops s on s.id = p.shop_id "+
                        "inner join product_attributes a on p.id = a.product_id " +
                        "where p.category = ? "+
                        "and s.country = ? " +
                        "and a.size = ? "+
                        "and p.name = ? "+
                        "order by name",
                result.getSqlWithoutParameters()
        );
        assertEquals(List.of("KITES", "EU", "10", "cabrinha"), result.getParamValues());
    }

    @Test
    public void distinctSize(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");
        filters.put("country", "US");

        ParameterizedStatement result = underTest.buildDistinctValuesSql(filters, "size");
        assertEquals(
                "select distinct a.size from products p " +
                        "inner join shops s on s.id = p.shop_id " +
                        "inner join product_attributes a on p.id = a.product_id " +
                        "where p.category = ? "+
                        "and s.country = ? " +
                        "and size <> 'unknown' "+
                        "order by size",
                result.getSqlWithoutParameters()
        );
        assertEquals(List.of("KITES", "US"), result.getParamValues());
    }

    @Test
    public void searchSql(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");
        filters.put("country", "UK");

        ParameterizedStatement result = underTest.buildSearchSql(filters, 2);
        assertEquals("select p.brand_name_version, p.link, a.price, a.size, p.condition, p.visible_to_public "+
                        "from products p " +
                        "inner join shops s on s.id = p.shop_id "+
                        "inner join product_attributes a on p.id = a.product_id " +
                        "where p.category = ? " +
                        "and s.country = ? "+
                        "order by a.price limit ? offset ?",
                result.getSqlWithoutParameters()
        );
        assertEquals(Arrays.asList("KITES", "UK", 21, 40), result.getParamValues());
    }

}
