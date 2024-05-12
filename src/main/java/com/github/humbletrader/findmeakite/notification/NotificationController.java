package com.github.humbletrader.findmeakite.notification;

import com.github.humbletrader.findmeakite.search.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

}
