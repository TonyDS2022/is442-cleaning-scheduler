package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CleaningSessionCalendarCardViewDto {

    @Getter
    private class WorkerListDto {
        private Long workerId;
        private String workerName;
        private String workerPhone;

        public WorkerListDto(Worker worker) {
            this.workerId = worker.getWorkerId();
            this.workerName = worker.getName();
            this.workerPhone = worker.getPhone();
        }
    }

    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private Double latitude;
    private Double longitude;
    private List<WorkerListDto> workers = new ArrayList<>();
    private LocalDate sessionStartDate;
    private CleaningSession.PlanningStage planningStage;
    private CleaningSession.SessionStatus sessionStatus;

    public CleaningSessionCalendarCardViewDto(CleaningSession cleaningSession) {
        this.clientName = cleaningSession.getClientSite().getClient().getName();
        this.clientPhone = cleaningSession.getClientSite().getClient().getPhone();
        this.clientAddress = cleaningSession.getClientSite().getLocation().getAddress();
        this.latitude = cleaningSession.getClientSite().getLocation().getLatitude();
        this.longitude = cleaningSession.getClientSite().getLocation().getLongitude();
        this.sessionStartDate = cleaningSession.getSessionStartDate();
        this.planningStage = cleaningSession.getPlanningStage();
        this.sessionStatus = cleaningSession.getSessionStatus();
        for (Shift shift : cleaningSession.getShifts()) {
            this.workers.add(new WorkerListDto(shift.getWorker()));
        }
    }
}
