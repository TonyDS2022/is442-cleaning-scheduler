package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.time.LocalTime;

@Configuration
@Getter
@PropertySource("classpath:cleaningSession.properties")
public class CleaningSessionConfigLoader {
    @Value("${cleaning-session.min-start-time}")
    private String minStartTime;
    @Value("${cleaning-session.max-end-time}")
    private String maxEndTime;
    @Value("${cleaning-session.start-lunch-time}")
    private String startLunchTime;
    @Value("${cleaning-session.end-lunch-time}")
    private String endLunchTime;
    @Value("${cleaning-session.start-dinner-time}")
    private String startDinnerTime;
    @Value("${cleaning-session.end-dinner-time}")
    private String endDinnerTime;

    protected static LocalTime MIN_START_TIME;
    protected static LocalTime MAX_END_TIME;
    protected static LocalTime START_LUNCH_TIME;
    protected static LocalTime END_LUNCH_TIME;
    protected static LocalTime START_DINNER_TIME;
    protected static LocalTime END_DINNER_TIME;

    @PostConstruct
    public void init() {
        initializeStaticFields(minStartTime, maxEndTime, startLunchTime, endLunchTime, startDinnerTime, endDinnerTime);
    }

    private static void initializeStaticFields(String minStartTime, String maxEndTime, String startLunchTime, String endLunchTime, String startDinnerTime, String endDinnerTime) {
        MIN_START_TIME = LocalTime.parse(minStartTime);
        MAX_END_TIME = LocalTime.parse(maxEndTime);
        START_LUNCH_TIME = LocalTime.parse(startLunchTime);
        END_LUNCH_TIME = LocalTime.parse(endLunchTime);
        START_DINNER_TIME = LocalTime.parse(startDinnerTime);
        END_DINNER_TIME = LocalTime.parse(endDinnerTime);
    }
}
