package com.github.humbletrader.findmeakite.search;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SearchServiceTest {

    private SearchService underTest = new SearchService();

    @Test
    public void sqlBuildForTwoParametersNonProductAttributes(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");
        filters.put("name", "cabrinha");

        ParameterizedStatement result = underTest.buildDistinctValuesSql(filters,   "name");
        assertEquals("select distinct p.name from products p where p.category = ? and p.name = ?", result.getSqlWithoutParameters());
        assertEquals(Arrays.asList("KITES", "cabrinha"), result.getParamValues());
    }

    @Test
    public void sqlShouldContainAJoinWhenSizeIsInFilter(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");
        filters.put("name", "cabrinha");
        filters.put("size", "10");

        ParameterizedStatement result = underTest.buildDistinctValuesSql(filters, "name");
        assertEquals("select distinct p.name " +
                "from products p inner join product_attributes a on p.id = a.product_id " +
                "where p.category = ? and a.size = ? and p.name = ?",
                result.getSqlWithoutParameters()
        );
        assertEquals(Arrays.asList("KITES", "10", "cabrinha"), result.getParamValues());
    }

    @Test
    public void distinctSize(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");

        ParameterizedStatement result = underTest.buildDistinctValuesSql(filters, "size");
        assertEquals("select distinct a.size from products p " +
                "inner join product_attributes a on p.id = a.product_id " +
                "where p.category = ?" ,
                result.getSqlWithoutParameters()
        );
        assertEquals(Arrays.asList("KITES"), result.getParamValues());
        assertEquals(Arrays.asList("KITES"), result.getParamValues());
    }

    @Test
    public void searchSql(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");

        ParameterizedStatement result = underTest.buildSearchSql(filters, 2);
        assertEquals("select p.brand_name_version, p.link, a.price, a.size from products p " +
                        "inner join product_attributes a on p.id = a.product_id " +
                        "where p.category = ? " +
                        "order by p.id limit ? offset ?",
                result.getSqlWithoutParameters()
        );
        assertEquals(Arrays.asList("KITES", 20, 40), result.getParamValues());
        assertEquals(Arrays.asList("KITES", 20, 40), result.getParamValues());
    }

}
