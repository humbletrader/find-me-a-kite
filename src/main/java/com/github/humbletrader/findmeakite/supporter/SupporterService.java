package com.github.humbletrader.findmeakite.supporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SupporterService {

    private static final Logger logger = LoggerFactory.getLogger(SupporterService.class);

    private Set<String> supporterTokens;

    public SupporterService(@Value("${fmak.supporter.tokens}")String supporterTokensAsString){
        logger.debug("accepted supporter tokens {}", supporterTokensAsString);
        this.supporterTokens = Set.of(supporterTokensAsString.split(","));
    }

    public boolean isSupporter(String token){
        return token != null && !supporterTokens.isEmpty() && supporterTokens.contains(token);
    }

}
