package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LeaveApplicationAdminViewDto {

    String workerName;
    String workerEmail;
    String workerPhoneNumber;
    LocalDate leaveStartDate;
    LocalDate leaveEndDate;
    List<AffectedShiftDto> affectedShifts;

    // Constructor that takes affectedShifts as a parameter
    public LeaveApplicationAdminViewDto(LeaveApplication leaveApplication, List<AffectedShiftDto> affectedShifts) {
        this.workerName = leaveApplication.getWorker().getName();
        this.workerEmail = leaveApplication.getWorker().getEmail();
        this.workerPhoneNumber = leaveApplication.getWorker().getPhone();
        this.leaveStartDate = leaveApplication.getLeaveStartDate();
        this.leaveEndDate = leaveApplication.getLeaveEndDate();
        this.affectedShifts = affectedShifts;  // Simply set the passed list
    }
}
