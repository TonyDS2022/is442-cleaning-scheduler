package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class CleaningSessionCalendarViewDto {
    private Long cleaningSessionId;
    private String clientName;
    private String subzoneName;
    private LocalDate sessionStartDate;
    private LocalDate sessionEndDate;
    private LocalTime sessionStartTime;
    private LocalTime sessionEndTime;
    private CleaningSession.PlanningStage planningStage;
    private CleaningSession.SessionStatus sessionStatus;
    private int workersBudgeted;

    public CleaningSessionCalendarViewDto(CleaningSession cleaningSession) {
        this.cleaningSessionId = cleaningSession.getCleaningSessionId();
        this.clientName = cleaningSession.getClientSite().getClient().getName();
        this.subzoneName = cleaningSession.getClientSite().getLocation().getSubzone().getSubzoneName();
        this.sessionStartDate = cleaningSession.getSessionStartDate();
        this.sessionEndDate = cleaningSession.getSessionEndDate();
        this.sessionStartTime = cleaningSession.getSessionStartTime();
        this.sessionEndTime = cleaningSession.getSessionEndTime();
        this.planningStage = cleaningSession.getPlanningStage();
        this.sessionStatus = cleaningSession.getSessionStatus();
        this.workersBudgeted = cleaningSession.getWorkersBudgeted();
    }
}
