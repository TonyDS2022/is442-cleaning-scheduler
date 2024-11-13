package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CleaningSessionCalendarCardViewDto {

    @Getter
    private class ShiftDto {
        private Long shiftId;
        private Long workerId;
        private String workerName;
        private String workerPhone;

        public ShiftDto(Shift shift) {
            this.shiftId = shift.getShiftId();
            this.workerId = shift.getWorker().getWorkerId();
            this.workerName = shift.getWorker().getName();
            this.workerPhone = shift.getWorker().getPhone();
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

    public CleaningSessionCalendarCardViewDto(CleaningSession cleaningSession) {
        this.clientName = cleaningSession.getClientSite().getClient().getName();
        this.clientPhone = cleaningSession.getClientSite().getClient().getPhone();
        this.clientAddress = cleaningSession.getClientSite().getLocation().getAddress();
        this.latitude = cleaningSession.getClientSite().getLocation().getLatitude();
        this.longitude = cleaningSession.getClientSite().getLocation().getLongitude();
        this.sessionStartDate = cleaningSession.getSessionStartDate();
        this.sessionEndDate = cleaningSession.getSessionEndDate();
        this.sessionStartTime = cleaningSession.getSessionStartTime();
        this.sessionEndTime = cleaningSession.getSessionEndTime();
        this.sessionStartDate = cleaningSession.getSessionStartDate();
        this.planningStage = cleaningSession.getPlanningStage();
        this.sessionStatus = cleaningSession.getSessionStatus();
        for (Shift shift : cleaningSession.getShifts()) {
            this.shifts.add(new ShiftDto(shift));
        }
    }
}
