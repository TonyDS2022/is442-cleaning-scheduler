package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.websocket.pojo.PojoEndpointBase;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplicationDto {
    private Long workerId;
    private String workerName;
    private Long adminId;
    private String adminName;
    private LeaveApplication.LeaveType leaveType;
    private LocalDate leaveStartDate;
    private LocalDate leaveEndDate;
    private LeaveApplication.ApplicationStatus applicationStatus;
    private LocalDate leaveSubmittedDate;
    private LocalTime leaveSubmittedTime;
}
