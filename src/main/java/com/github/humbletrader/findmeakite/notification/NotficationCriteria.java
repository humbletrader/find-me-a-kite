package com.github.humbletrader.findmeakite.notification;


import com.github.humbletrader.fmak.query.SearchValAndOp;

import java.util.Map;

public class NotficationCriteria {

    private final String email;
    private final Map<String, SearchValAndOp> criteria;
    private final String supporterToken;

    public NotficationCriteria(String email,
                                Map<String, SearchValAndOp> criteria,
                                String supporterToken){
        this.email = email;
        this.criteria = criteria;
        this.supporterToken = supporterToken;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, SearchValAndOp> getCriteria() {
        return criteria;
    }

    public String getSupporterToken() {
        return supporterToken;
    }
}
