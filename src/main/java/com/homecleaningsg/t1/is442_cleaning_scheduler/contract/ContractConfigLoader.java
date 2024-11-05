package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@Getter
@PropertySource("classpath:contract.properties")
public class ContractConfigLoader {
    @Value("${contract.minSessionDurationMinutes}")
    private int minSessionDurationMinutes;
    @Value("${contract.maxSessionDurationMinutes}")
    private int maxSessionDurationMinutes;

    protected static int MIN_SESSION_DURATION_MINUTES;
    protected static int MAX_SESSION_DURATION_MINUTES;

    @PostConstruct
    public void init(){
        initializeStaticFields(minSessionDurationMinutes, maxSessionDurationMinutes);
    }

    private static void initializeStaticFields(int minSessionDurationMinutes, int maxSessionDurationMinutes){
        MIN_SESSION_DURATION_MINUTES = minSessionDurationMinutes;
        MAX_SESSION_DURATION_MINUTES = maxSessionDurationMinutes;
    }
}
