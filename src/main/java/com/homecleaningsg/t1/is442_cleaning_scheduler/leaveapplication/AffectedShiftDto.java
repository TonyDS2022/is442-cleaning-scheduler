package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;

import java.time.LocalDate;
import java.time.LocalTime;

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
