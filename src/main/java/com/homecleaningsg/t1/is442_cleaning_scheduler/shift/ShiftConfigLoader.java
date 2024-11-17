package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import jakarta.annotation.PostConstruct;

@Configuration
@Getter
@PropertySource("classpath:shift.properties")
public class ShiftConfigLoader {
    @Value("${shift.weeklyOverTimeThresholdHours}")
    private int weeklyOverTimeThresholdHours;

    protected static int WEEKLY_OVERTIME_THRESHOLD_HOURS;

    @PostConstruct
    public void init(){
        initializeStaticFields(weeklyOverTimeThresholdHours);
    }

    private static void initializeStaticFields(int weeklyOverTimeThresholdHours){
        WEEKLY_OVERTIME_THRESHOLD_HOURS = weeklyOverTimeThresholdHours;
    }
}