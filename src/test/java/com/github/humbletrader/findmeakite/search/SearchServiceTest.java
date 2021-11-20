package com.github.humbletrader.findmeakite.search;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public class SearchServiceTest {

    private SearchService underTest = new SearchService();


    @Test
    public void sqlBuild(){
        Map<String, String> filters = new HashMap<>();
        filters.put("category", "KITES");
        filters.put("name", "cabrinha");

        SearchStatement result = underTest.buildSqlWithFilters(filters, OptionalInt.of(1), "p.name");
        System.out.println(result);

    }

}
