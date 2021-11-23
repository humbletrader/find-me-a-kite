package com.github.humbletrader.findmeakite.search;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SearchServiceTest {

    private SearchService underTest = new SearchService();

    @Test
    public void sqlBuildForTwoParametersNonProductAttributes(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");
        filters.put("name", "cabrinha");

        SearchStatement result = underTest.buildSqlWithFilters(filters, OptionalInt.of(0), false, "name");
        assertEquals("select p.name from products p where p.category = ? and p.name = ? order by p.id limit ? offset ?", result.getSqlWithoutParameters());
        assertTrue(Arrays.equals(new Object[]{"KITES", "cabrinha", 20, 0}, result.getParamValues()));
    }

    @Test
    public void sqlShouldContainAJoinWhenSizeIsInFilter(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");
        filters.put("name", "cabrinha");
        filters.put("size", "10");

        SearchStatement result = underTest.buildSqlWithFilters(filters, OptionalInt.of(0), false,"name");
        assertEquals("select p.name " +
                "from products p inner join product_attributes a on p.id = a.product_id " +
                "where p.category = ? and a.size = ? and p.name = ? " +
                "order by p.id limit ? offset ?",
                result.getSqlWithoutParameters()
        );
        assertTrue(Arrays.equals(new Object[]{"KITES", "10", "cabrinha", 20, 0}, result.getParamValues()));
    }

    @Test
    public void distinctSize(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");

        SearchStatement result = underTest.buildSqlWithFilters(filters, OptionalInt.of(2), true, "size");
        assertEquals("select distinct a.size from products p " +
                "inner join product_attributes a on p.id = a.product_id " +
                "where p.category = ? " +
                        "order by p.id limit ? offset ?",
                result.getSqlWithoutParameters()
        );
        assertTrue(Arrays.equals(new Object[]{"KITES", 20, 40}, result.getParamValues()));
    }

}
