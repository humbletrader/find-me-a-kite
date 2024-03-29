package com.github.humbletrader.findmeakite.search;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Math.min;

@Service
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    public final static int ROWS_DISPLAYED_PER_PAGE = 20;

    private final static Set<String> PRODUCT_ATTRIBUTES_COLUMNS = Set.of("price", "size", "color");

    private Set<String> supporterTokens;


    public SearchService(@Value("${fmak.supporter.tokens}")String supporterTokensAsString){
        logger.info("accepted supporter tokens {}", supporterTokensAsString);
        this.supporterTokens = Set.of(supporterTokensAsString.split(","));
    }

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

    public SearchResultPage searchByCriteria(SearchCriteria criteria){
        logger.info("searching products by criteria {} ", criteria);

        ParameterizedStatement sql = buildSearchSql(criteria.getCriteria(), criteria.getPage());
        List<SearchItem> result = searchRepository.pagedSearchByCriteria(sql);

        boolean hasNextPage = result.size() > ROWS_DISPLAYED_PER_PAGE;
        logger.info("search returned {} items this means next page is available {}", result.size(), hasNextPage);

        //obfuscate the items for non supporters
        List<SearchItem> obfuscatedResult = obfuscateAndLimitResults(result, criteria.getSupporterToken());
        return new SearchResultPage(criteria.getPage(), obfuscatedResult, hasNextPage);
    }

    /**
     * 1. checks the visibility of each output item and transforms the result for non supporters
     * 2. limits the size of the output to the maximum accepted per page
     * @param queryResults
     * @return
     */
    private List<SearchItem> obfuscateAndLimitResults(List<SearchItem> queryResults, String token){
        return queryResults.stream()
                .map(searchItem -> {
                    boolean hiddenItem = !searchItem.isVisibleToPublic();
                    boolean userIsASupporter = token != null && !supporterTokens.isEmpty() && supporterTokens.contains(token);

                    if(hiddenItem && !userIsASupporter){
                        return new SearchItem(
                                "item visible to supporters only",
                                "",
                                searchItem.getPrice(),
                                searchItem.getSize(),
                                searchItem.getCondition(),
                                searchItem.isVisibleToPublic()
                        );
                    } else return searchItem;
                })
                .limit(ROWS_DISPLAYED_PER_PAGE)
                .toList();
    }

    /**
     * builds the sql for 'distinct values call"
     * @param criteria the criteria for the search
     * @param column    the column for which we check the distinct values
     * @return  the sql statement to be executed in order to get the distinct values from db
     */
    ParameterizedStatement buildDistinctValuesSql(Map<String, String> criteria, String column){

        StringBuilder selectString = new StringBuilder("select distinct");

        boolean isColumnFromProductAttributes = isProductAttributeTableColumn(column);
        selectString.append(prefixedColumn(column));


        StringBuilder fromString = new StringBuilder(" from products p");
        fromString.append(" inner join shops s on s.id = p.shop_id");
        if(isColumnFromProductAttributes || !Sets.intersection(criteria.keySet(), PRODUCT_ATTRIBUTES_COLUMNS).isEmpty()){
            fromString.append(" inner join product_attributes a on p.id = a.product_id");
        }
        selectString.append(fromString);

        ParameterizedStatement whereParameterizedStatement = whereFromCriteria(criteria);
        selectString.append(whereParameterizedStatement.getSqlWithoutParameters());
        selectString.append(avoidForbiddenValues(column));
        selectString.append(" order by ").append(column);
        return new ParameterizedStatement(selectString.toString(), whereParameterizedStatement.getParamValues());
    }

    /**
     * builds the sql statement to retrieve the db items for the given criteria
     * @param criteria  the criteria (ie. brand=DUOTONE, etc)
     * @param page  the new page requested
     * @return  the sql to be executed against the db
     */
    ParameterizedStatement buildSearchSql(Map<String, String> criteria, int page) {
        //"brand_name_version", "link", "price", "size"
        StringBuilder select = new StringBuilder("select");
        select.append(" p.brand_name_version, p.link, a.price, a.size, p.condition, p.visible_to_public");
        select.append(" from products p");
        select.append(" inner join shops s on s.id = p.shop_id");
        select.append(" inner join product_attributes a on p.id = a.product_id");

        ParameterizedStatement whereStatement = whereFromCriteria(criteria);
        select.append(whereStatement.getSqlWithoutParameters());
        List<Object> valuesForParameters = whereStatement.getParamValues();

        int startOfPage = page * ROWS_DISPLAYED_PER_PAGE;
        select.append(" order by a.price limit ? offset ?");
        valuesForParameters.add(ROWS_DISPLAYED_PER_PAGE + 1); //request one more row to detect if there is a next page available
        valuesForParameters.add(startOfPage);

        return new ParameterizedStatement(select.toString(), valuesForParameters);
    }

    /**
     * builds the where clause of the sql for the given criteria
     * @param criteria  the criteria
     * @return  a part of sql with the "where" clause
     */
    ParameterizedStatement whereFromCriteria(Map<String, String> criteria){
        StringBuilder whereString = new StringBuilder(" where");
        List<Object> valuesForParameters = new ArrayList<>();

        //first we build sql for the mandatory params ( category, country )
        whereString.append(" p.category = ?");
        valuesForParameters.add(criteria.get("category"));

        whereString.append(" and s.country = ?");
        valuesForParameters.add(criteria.get("country"));

        //then we check the rest
        for (Map.Entry<String, String> currentCriteria : criteria.entrySet()) {
            String currentKey = currentCriteria.getKey();
            if(!currentKey.equals("category") && !currentKey.equals("country")){
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

    @Deprecated //todo: as the parser gets better we don't need this method anymore
    String avoidForbiddenValues(String column){
        return
        switch(column){
            case "brand" -> " and brand <> 'unknown'";
            case "year" -> " and year <> -1 and year <> -2";
            case "version" -> " and version <> 'not needed' and version <> 'unknown'";
            case "size" -> " and size <> 'unknown'";
            case "product_name", "condition", "subprod_name" -> "";
            default -> throw new RuntimeException("impossible to avoid forbidden values for column " + column);
        };
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
