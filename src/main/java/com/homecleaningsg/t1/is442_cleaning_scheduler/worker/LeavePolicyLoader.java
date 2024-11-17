package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import jakarta.annotation.PostConstruct;

import java.time.MonthDay;

@Configuration
@Getter
@PropertySource("classpath:leave-policy.properties")
public class LeavePolicyLoader {
    @Value("${leave-policy.max-annual-leave-days}")
    private int maxAnnualLeaveDays;
    @Value("${leave-policy.max-medical-leave-days}")
    private int maxMedicalLeaveDays;
    @Value("${leave-policy.year-start-date}")
    private String yearStartDate;

    protected static int MAX_ANNUAL_LEAVE_DAYS;
    protected static int MAX_MEDICAL_LEAVE_DAYS;
    protected static MonthDay YEAR_START_DATE;

    @PostConstruct
    public void init(){
        initializeStaticFields(maxAnnualLeaveDays, maxMedicalLeaveDays, yearStartDate);
    }

    private static void initializeStaticFields(int maxAnnualLeaveDays, int maxMedicalLeaveDays, String yearStartDate){
        MAX_ANNUAL_LEAVE_DAYS = maxAnnualLeaveDays;
        MAX_MEDICAL_LEAVE_DAYS = maxMedicalLeaveDays;
        YEAR_START_DATE = MonthDay.parse(yearStartDate);
    }

    public static long getMaxLeaveDays(LeaveApplication.LeaveType leaveType){
        return switch (leaveType){
            case MEDICAL -> MAX_ANNUAL_LEAVE_DAYS;
            case ANNUAL -> MAX_MEDICAL_LEAVE_DAYS;
        };
    }
}
