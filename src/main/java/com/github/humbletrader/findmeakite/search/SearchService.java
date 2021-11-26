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
        ParameterizedStatement searchStatement = buildDistinctValuesSql(criteria.getCriteria(), criteria.getTarget());
        return searchRepository.searchDistinctValues(searchStatement);
    }

    public List<SearchResult> searchByCriteriaV2(SearchCriteriaV2 criteria){
        logger.info("searching products by criteria {} ", criteria);

        ParameterizedStatement sql = buildSearchSql(criteria.getCriteria(), criteria.getPage());

        return searchRepository.pagedSearchByCriteriaV2(sql);
    }

    ParameterizedStatement buildDistinctValuesSql(Map<String, String> criteria, String column){

        StringBuilder selectString = new StringBuilder("select distinct");

        boolean isColumnFromProductionAttribute = isProductAttributeTableColumn(column);
        selectString.append(prefixedColumn(column));


        StringBuilder fromString = new StringBuilder(" from products p");

        if(isColumnFromProductionAttribute || !Sets.intersection(criteria.keySet(), PRODUCT_ATTRIBUTES_COLUMNS).isEmpty()){
            fromString.append(" inner join product_attributes a on p.id = a.product_id");
        }
        selectString.append(fromString);

        ParameterizedStatement whereParameterizedStatement = whereFromCriteria(criteria);
        selectString.append(whereParameterizedStatement.getSqlWithoutParameters());

        return new ParameterizedStatement(selectString.toString(), whereParameterizedStatement.getParamValues());
    }

    ParameterizedStatement buildSearchSql(Map<String, String> criteria, int page) {
        //"brand_name_version", "link", "price", "size"
        StringBuilder select = new StringBuilder("select");
        select.append(" p.brand_name_version, p.link, a.price, a.size");
        select.append(" from products p inner join product_attributes a");
        select.append(" on p.id = a.product_id");

        ParameterizedStatement whereStatement = whereFromCriteria(criteria);
        select.append(whereStatement.getSqlWithoutParameters());
        List<Object> valuesForParameters = whereStatement.getParamValues();

        int startOfPage = page * ROWS_PER_PAGE;
        select.append(" order by p.id limit ? offset ?");
        valuesForParameters.add(ROWS_PER_PAGE);
        valuesForParameters.add(startOfPage);

        return new ParameterizedStatement(select.toString(), valuesForParameters);
    }

    ParameterizedStatement whereFromCriteria(Map<String, String> criteria){
        StringBuilder whereString = new StringBuilder(" where");
        List<Object> valuesForParameters = new ArrayList<>();

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

        return new ParameterizedStatement(whereString.toString(), valuesForParameters);
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
