package com.github.humbletrader.findmeakite.search;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    public final static int ROWS_PER_PAGE = 20;

    private final static Set<String> PRODUCT_ATTRIBUTES_COLUMNS = new HashSet<>(Arrays.asList("price", "size", "color"));
    @Autowired
    private SearchRepository searchRepository;

    public List<String> findCategories(){
        logger.info("servicing supported categories ...");
        return Arrays.asList("KITE", "TWINTIPS", "BARS", "WETSUITS");
    }

    public List<String> searchDistinctValuesByCriteria(DistinctValuesSearchCriteria criteria){
        SearchStatement searchStatement = buildSqlWithFilters(criteria.getCriteria(), OptionalInt.empty(), true, criteria.getTarget());
        return searchRepository.searchDistinctValues(searchStatement);
    }

    public List<SearchResult> searchByCriteriaV2(SearchCriteriaV2 criteria){
        logger.info("searching products by criteria {} ", criteria);

        SearchStatement sql = buildSqlWithFilters(criteria.getCriteria(), OptionalInt.of(criteria.getPage()), false, "name", "link", "brand_name_version");

        return searchRepository.pagedSearchByCriteriaV2(sql);
    }

    SearchStatement buildSqlWithFilters(Map<String, String> criteria, OptionalInt page, boolean distinct, String... columns){

        StringBuilder selectString = new StringBuilder("select");
        if(distinct) selectString.append(" distinct");
        boolean hasColumnsFromProductAttributesInSelect = false;
        for (int i = 0; i < columns.length; i++) {
            if(isProductAttributeTableColumn(columns[i])){
               hasColumnsFromProductAttributesInSelect = true;
            }
            selectString.append(prefixedColumn(columns[i]));
            if (i < columns.length - 1) {
                selectString.append(",");
            }
        }

        StringBuilder fromString = new StringBuilder(" from");
        StringBuilder whereString = new StringBuilder(" where");
        List<Object> valuesForParameters = new ArrayList<>();

        fromString.append(" products p");
        if(hasColumnsFromProductAttributesInSelect || !Sets.intersection(criteria.keySet(), PRODUCT_ATTRIBUTES_COLUMNS).isEmpty()){
            fromString.append(" inner join product_attributes a on p.id = a.product_id");
        }
        selectString.append(fromString);

        whereString.append(" p.category = ?");
        valuesForParameters.add(criteria.get("category"));

        for (Map.Entry<String, String> currentCriteria : criteria.entrySet()) {
            if(!currentCriteria.getKey().equals("category")){
                whereString.append(" and").append(prefixedColumn(currentCriteria.getKey())).append(" = ?");
                if(currentCriteria.getKey().equals("year")){
                    //year is an integer in DB (temporary)
                    valuesForParameters.add(Integer.valueOf(currentCriteria.getValue()));
                }else{
                    valuesForParameters.add(currentCriteria.getValue().toLowerCase());
                }
            }
        }
        selectString.append(whereString);

        if(page.isPresent()){
            int startOfPage = page.getAsInt() * ROWS_PER_PAGE;
            selectString.append(" order by p.id limit ? offset ?");
            valuesForParameters.add(ROWS_PER_PAGE);
            valuesForParameters.add(startOfPage);
        }

        return new SearchStatement(selectString.toString(), valuesForParameters.toArray());
    }

    private boolean isProductAttributeTableColumn(String colName){
        return PRODUCT_ATTRIBUTES_COLUMNS.contains(colName);
    }

    private String prefixedColumn(String column){
        if (isProductAttributeTableColumn(column)) {
            return " a."+column;
        }else{
            return " p."+column;
        }
    }

}
