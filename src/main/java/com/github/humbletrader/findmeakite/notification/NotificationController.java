package com.github.humbletrader.findmeakite.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "/notification")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @PostMapping(path = "/save")
    public ResponseEntity<SaveNotificationResult> saveNotification(@RequestBody NotficationCriteria criteria) {
        logger.info("saving notification for {} ", criteria);
        return new ResponseEntity<>(notificationService.saveNotification(criteria), HttpStatus.OK);
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<NotificationDbEntity>> listNotifications(
            @RequestParam String supporterToken,
            @RequestParam String email) {
        logger.info("listing notifications for email {}", email);
        return ResponseEntity.ok(notificationService.listNotifications(supporterToken, email));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<SaveNotificationResult> deleteNotification(
            @PathVariable long id,
            @RequestParam String supporterToken) {
        logger.info("deleting notification {}", id);
        return ResponseEntity.ok(notificationService.deleteNotification(supporterToken, id));
    }

}
