package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.time.LocalTime;

@Configuration
@Getter
@PropertySource("classpath:contract.properties")
public class ContractConfigLoader {
    @Value("${contract.min-start-time}")
    private String minStartTime;
    @Value("${contract.max-end-time}")
    private String maxEndTime;
    @Value("${contract.start-lunch-time}")
    private String startLunchTime;
    @Value("${contract.end-lunch-time}")
    private String endLunchTime;
    @Value("${contract.start-dinner-time}")
    private String startDinnerTime;
    @Value("${contract.end-dinner-time}")
    private String endDinnerTime;
    @Value("${contract.minSessionDurationHours}")
    private int minSessionDurationHours;
    @Value("${contract.maxSessionDurationHours}")
    private int maxSessionDurationHours;

    protected static int MIN_SESSION_DURATION_HOURS;
    protected static int MAX_SESSION_DURATION_HOURS;
    protected static LocalTime MIN_START_TIME;
    protected static LocalTime MAX_END_TIME;
    protected static LocalTime START_LUNCH_TIME;
    protected static LocalTime END_LUNCH_TIME;
    protected static LocalTime START_DINNER_TIME;
    protected static LocalTime END_DINNER_TIME;


    @PostConstruct
    public void init(){
        initializeStaticFields(minStartTime, maxEndTime, startLunchTime, endLunchTime, startDinnerTime, endDinnerTime, minSessionDurationHours, maxSessionDurationHours);
    }

    private static void initializeStaticFields(String minStartTime, String maxEndTime, String startLunchTime, String endLunchTime, String startDinnerTime, String endDinnerTime, int minSessionDurationHours, int maxSessionDurationHours){
        MIN_START_TIME = LocalTime.parse(minStartTime);
        MAX_END_TIME = LocalTime.parse(maxEndTime);
        START_LUNCH_TIME = LocalTime.parse(startLunchTime);
        END_LUNCH_TIME = LocalTime.parse(endLunchTime);
        START_DINNER_TIME = LocalTime.parse(startDinnerTime);
        END_DINNER_TIME = LocalTime.parse(endDinnerTime);
        MIN_SESSION_DURATION_HOURS = minSessionDurationHours;
        MAX_SESSION_DURATION_HOURS = maxSessionDurationHours;
    }
}
