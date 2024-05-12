package com.github.humbletrader.findmeakite.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationRepository {

    private static final Logger logger = LoggerFactory.getLogger(NotificationRepository.class);

    private JdbcTemplate jdbcTemplate;

    public NotificationRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveNotification(NotificationDbEntity notification){
        logger.info("saving notification {}", notification);
        jdbcTemplate.update("insert into notifications(email, query_as_json) values ( '"+notification.email() +"', '"+notification.queryAsJson()+"' )");
    }

}
