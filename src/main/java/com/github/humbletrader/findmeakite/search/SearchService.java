package com.github.humbletrader.findmeakite.search;

import com.github.humbletrader.findmeakite.supporter.SupporterService;
import com.github.humbletrader.fmak.query.FmakSqlBuilder;
import com.github.humbletrader.fmak.query.ParameterizedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;



@Service
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    public final static int ROWS_DISPLAYED_PER_PAGE = 20;

    private final SearchRepository searchRepository;
    private final SupporterService supporterService;
    private final FmakSqlBuilder sqlBuilder = new FmakSqlBuilder(ROWS_DISPLAYED_PER_PAGE);


    public SearchService(SupporterService supporterService, SearchRepository searchRepository){
        this.supporterService = supporterService;
        this.searchRepository = searchRepository;
    }

    public List<String> findCategories(){
        logger.info("servicing supported categories ...");
        return Arrays.asList("KITE", "TWINTIPS", "BARS", "WETSUITS");
    }

    public List<String> searchDistinctValuesByCriteria(DistinctValuesSearchCriteria criteria){
        ParameterizedStatement searchStatement = sqlBuilder.buildDistinctValuesSql(criteria.getCriteria(), criteria.getTarget());
        return searchRepository.searchDistinctValues(searchStatement);
    }

    public SearchResultPage searchByCriteria(SearchCriteria criteria){
        logger.info("searching products by criteria {} ", criteria);

        ParameterizedStatement sql = sqlBuilder.buildSearchSqlForWebFilters(criteria.getCriteria(), criteria.getPage());
        List<SearchResultItem> result = searchRepository.pagedSearchByCriteria(sql);

        boolean hasNextPage = result.size() > ROWS_DISPLAYED_PER_PAGE;
        logger.info("search returned {} items this means next page is available {}", result.size(), hasNextPage);

        //obfuscate the items for non supporters
        List<SearchResultItem> obfuscatedResult = obfuscateAndLimitResults(result, criteria.getSupporterToken());
        return new SearchResultPage(criteria.getPage(), obfuscatedResult, hasNextPage);
    }

    /**
     * 1. checks the visibility of each output item and transforms the result for non supporters
     * 2. limits the size of the output to the maximum accepted per page
     * @param queryResults
     * @return
     */
    private List<SearchResultItem> obfuscateAndLimitResults(List<SearchResultItem> queryResults, String token){
        return queryResults.stream()
                .map(searchResultItem -> {
                    boolean hiddenItem = !searchResultItem.isVisibleToPublic();
                    boolean userIsASupporter = supporterService.isSupporter(token);

                    if(hiddenItem && !userIsASupporter){
                        return new SearchResultItem(
                                "item visible to supporters only",
                                "",
                                searchResultItem.getPrice(),
                                searchResultItem.getSize(),
                                searchResultItem.getCondition(),
                                searchResultItem.isVisibleToPublic()
                        );
                    } else return searchResultItem;
                })
                .limit(ROWS_DISPLAYED_PER_PAGE)
                .toList();
    }

}
