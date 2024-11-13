package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LeaveApplicationAdminViewDto {

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    public class AffectedShiftDto {
        Long shiftId;
        LocalDate shiftStartDate;
        LocalDate shiftEndDate;
        LocalTime shiftStartTime;
        LocalTime shiftEndTime;
        String clientName;
        String clientPhone;

        public AffectedShiftDto(Shift shift) {
            this.shiftId = shift.getShiftId();
            this.shiftStartDate = shift.getSessionStartDate();
            this.shiftEndDate = shift.getSessionEndDate();
            this.shiftStartTime = shift.getSessionStartTime();
            this.shiftEndTime = shift.getSessionEndTime();
            this.clientName = shift.getClientSite().getClient().getName();
            this.clientPhone = shift.getClientSite().getClient().getPhone();
        }
    }

    String workerName;
    String workerEmail;
    String workerPhoneNumber;
    LocalDate leaveStartDate;
    LocalDate leaveEndDate;
    List<AffectedShiftDto> affectedShifts = new ArrayList<>();

    public LeaveApplicationAdminViewDto(LeaveApplication leaveApplication) {
        this.workerName = leaveApplication.getWorker().getName();
        this.workerEmail = leaveApplication.getWorker().getEmail();
        this.workerPhoneNumber = leaveApplication.getWorker().getPhone();
        this.leaveStartDate = leaveApplication.getLeaveStartDate();
        this.leaveEndDate = leaveApplication.getLeaveEndDate();
        for (Shift shift : leaveApplicationService.getShiftsAffectedByLeaveApplication(leaveApplication)) {
            affectedShifts.add(new AffectedShiftDto(shift));
        }
    }

}
