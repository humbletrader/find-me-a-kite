package com.github.humbletrader.findmeakite.notification;

public record NotificationDbEntity(Long id, String email, String queryAsJson, int runCount){
}
