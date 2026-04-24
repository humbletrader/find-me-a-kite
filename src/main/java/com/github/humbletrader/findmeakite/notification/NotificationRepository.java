package com.github.humbletrader.findmeakite.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepository {

    private static final Logger logger = LoggerFactory.getLogger(NotificationRepository.class);

    private JdbcTemplate jdbcTemplate;

    public NotificationRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveNotification(NotificationDbEntity notification){
        logger.info("saving notification {}", notification);
        jdbcTemplate.update(
                "insert into notifications(email, query_as_json, run_count) values ( ?, ?, ? ) ",
                notification.email(),
                notification.queryAsJson(),
                notification.runCount()
        );
    }

    public List<NotificationDbEntity> findByEmail(String email) {
        return jdbcTemplate.query(
                "select id, email, query_as_json, run_count from notifications where email = ?",
                (rs, rowNum) -> new NotificationDbEntity(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("query_as_json"),
                        rs.getInt("run_count")
                ),
                email
        );
    }

    public void deleteById(long id) {
        jdbcTemplate.update("delete from notifications where id = ?", id);
    }

}
