package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkerPendingLeaveDto extends Shift {
    private boolean workerHasPendingLeave;

    public WorkerPendingLeaveDto(Shift shift, boolean workerHasPendingLeave) {
        super(shift.getCleaningSession());
        this.setShiftId(shift.getShiftId());
        this.setClientSite(shift.getClientSite());
        this.setSessionDescription(shift.getSessionDescription());
        this.setWorker(shift.getWorker());
        this.setSessionStartDate(shift.getSessionStartDate());
        this.setSessionStartTime(shift.getSessionStartTime());
        this.setSessionEndDate(shift.getSessionEndDate());
        this.setSessionEndTime(shift.getSessionEndTime());
        this.setActualStartDate(shift.getActualStartDate());
        this.setActualStartTime(shift.getActualStartTime());
        this.setActualEndDate(shift.getActualEndDate());
        this.setActualEndTime(shift.getActualEndTime());
        this.setWorkingStatus(shift.getWorkingStatus());
        this.setStartAcknowledge(shift.getStartAcknowledge());
        this.setEndAcknowledge(shift.getEndAcknowledge());
        this.workerHasPendingLeave = workerHasPendingLeave;
    }
}