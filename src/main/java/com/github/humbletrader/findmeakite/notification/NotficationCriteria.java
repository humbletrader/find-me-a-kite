package com.github.humbletrader.findmeakite.notification;


import com.github.humbletrader.fmak.query.SearchValAndOp;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SequencedSet;

public class NotficationCriteria {

    private final String email;
    private final Map<String, List<SearchValAndOp>> criteria;
    private final String supporterToken;

    public NotficationCriteria(String email,
                                Map<String, List<SearchValAndOp>> criteria,
                                String supporterToken){
        this.email = email;
        this.criteria = criteria;
        this.supporterToken = supporterToken;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, SequencedSet<SearchValAndOp>> getCriteria() {
        return criteria.entrySet().stream().collect(
                java.util.stream.Collectors.toMap(
                        java.util.Map.Entry::getKey,
                        e -> new LinkedHashSet<>(e.getValue())
                )
        );
    }

    public String getSupporterToken() {
        return supporterToken;
    }
}
