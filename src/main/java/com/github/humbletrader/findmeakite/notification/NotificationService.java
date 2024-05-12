package com.github.humbletrader.findmeakite.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.humbletrader.findmeakite.search.SearchCriteria;
import com.github.humbletrader.findmeakite.supporter.SupporterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private NotificationRepository notificationRepo;
    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    private SupporterService supporterService;

    public NotificationService(NotificationRepository notificationRepository, SupporterService supporterService){
        this.notificationRepo = notificationRepository;
        this.supporterService = supporterService;
    }

    public SaveNotificationResult saveNotification(NotficationCriteria searchCriteria){
        if(supporterService.isSupporter(searchCriteria.getSupporterToken())){
            var searchCriteriaAsObject = searchCriteria.getCriteria();
            try {
                var searchCriteriaAsJson = jsonObjectMapper.writeValueAsString(searchCriteriaAsObject);
                notificationRepo.saveNotification(new NotificationDbEntity(searchCriteria.getEmail(), searchCriteriaAsJson));
                return new SaveNotificationResult("success", "notification successfully saved");

            } catch (JsonProcessingException jsonExc){
                logger.error("error writing json for db ", jsonExc);
                return new SaveNotificationResult("fail", "error processing json query");
            }
        } else {
            return new SaveNotificationResult("fail", "user is not supporter");
        }



    }

}
