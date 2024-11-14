package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class CleaningSessionCalendarCardViewDto {

    @Getter
    private class ShiftDto {
        private Long shiftId;
        private Long workerId;
        private String workerName;
        private String workerPhone;
        private boolean workerHasPendingLeave;

        public ShiftDto(Shift shift, Map<Long, Boolean> workerLeaveStatusMap) {
            this.shiftId = shift.getShiftId();
            Worker worker = shift.getWorker();
            if (worker != null) {
                this.workerId = worker.getWorkerId();
                this.workerName = worker.getName();
                this.workerPhone = worker.getPhone();
                // Check the leave status from the map
                this.workerHasPendingLeave = workerLeaveStatusMap.getOrDefault(worker.getWorkerId(), false);
            } else {
                this.workerHasPendingLeave = false; // Default to false if there's no worker assigned
            }
        }
    }

    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private Double latitude;
    private Double longitude;
    private LocalDate sessionStartDate;
    private LocalDate sessionEndDate;
    private LocalTime sessionStartTime;
    private LocalTime sessionEndTime;
    private List<ShiftDto> shifts = new ArrayList<>();
    private CleaningSession.PlanningStage planningStage;
    private CleaningSession.SessionStatus sessionStatus;

    public CleaningSessionCalendarCardViewDto(CleaningSession cleaningSession, Map<Long, Boolean> workerLeaveStatusMap) {
        this.clientName = cleaningSession.getClientSite().getClient().getName();
        this.clientPhone = cleaningSession.getClientSite().getClient().getPhone();
        this.clientAddress = cleaningSession.getClientSite().getLocation().getAddress();
        this.latitude = cleaningSession.getClientSite().getLocation().getLatitude();
        this.longitude = cleaningSession.getClientSite().getLocation().getLongitude();
        this.sessionStartDate = cleaningSession.getSessionStartDate();
        this.sessionEndDate = cleaningSession.getSessionEndDate();
        this.sessionStartTime = cleaningSession.getSessionStartTime();
        this.sessionEndTime = cleaningSession.getSessionEndTime();
        this.planningStage = cleaningSession.getPlanningStage();
        this.sessionStatus = cleaningSession.getSessionStatus();

        // Populate ShiftDto list with leave status
        for (Shift shift : cleaningSession.getShifts()) {
            this.shifts.add(new ShiftDto(shift, workerLeaveStatusMap));
        }
    }
}