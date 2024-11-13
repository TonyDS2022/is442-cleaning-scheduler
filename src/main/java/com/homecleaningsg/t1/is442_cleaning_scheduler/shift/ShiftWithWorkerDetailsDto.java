package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
public class ShiftWithWorkerDetailsDto {
    private Long shiftId;
    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private Double latitude;
    private Double longitude;
    private String workerName;
    private String workerPhone;
    private String workerEmail;
    private String workerBio;
    private String sessionDescription;
    private LocalDate sessionStartDate;
    private LocalTime sessionStartTime;
    private LocalDate sessionEndDate;
    private LocalTime sessionEndTime;
    private LocalDate actualStartDate;
    private LocalTime actualStartTime;
    private LocalDate actualEndDate;
    private LocalTime actualEndTime;
    private String workingStatus;
    private boolean workerHasPendingLeave;

    public ShiftWithWorkerDetailsDto(Shift shift) {
        this.shiftId = shift.getShiftId();
        this.clientName = shift.getCleaningSession().getClientSite().getClient().getName();
        this.clientPhone = shift.getCleaningSession().getClientSite().getClient().getPhone();
        this.clientAddress = shift.getCleaningSession().getClientSite().getStreetAddress();
        this.latitude = shift.getCleaningSession().getClientSite().getLocation().getLatitude();
        this.longitude = shift.getCleaningSession().getClientSite().getLocation().getLongitude();
        Worker worker = shift.getWorker();
        if (worker != null) {
            this.workerName = worker.getName();
            this.workerPhone = worker.getPhone();
            this.workerEmail = worker.getEmail();
            this.workerBio = worker.getBio();
        }
        this.sessionDescription = shift.getSessionDescription();
        this.sessionStartDate = shift.getSessionStartDate();
        this.sessionStartTime = shift.getSessionStartTime();
        this.sessionEndDate = shift.getSessionEndDate();
        this.sessionEndTime = shift.getSessionEndTime();
        this.actualStartDate = shift.getActualStartDate();
        this.actualStartTime = shift.getActualStartTime();
        this.actualEndDate = shift.getActualEndDate();
        this.actualEndTime = shift.getActualEndTime();
        this.workingStatus = shift.getWorkingStatus().name();
        this.workerHasPendingLeave = shift.isWorkerHasPendingLeave();
    }
}
