package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.Trip;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ShiftWithWorkerDetailAndTripDto {

    @Getter
    @NoArgsConstructor
    public class WorkerDetailsDto {
        private Long workerId;
        private String workerName;
        private String workerPhone;
        private String workerBio;
        private boolean workerHasPendingLeave;

        public WorkerDetailsDto(Worker worker, boolean workerHasPendingLeave) {
            this.workerId = worker.getWorkerId();
            this.workerName = worker.getName();
            this.workerPhone = worker.getPhone();
            this.workerBio = worker.getBio();
            this.workerHasPendingLeave = workerHasPendingLeave;
        }
    }

    @Getter
    public class LocationDto {
        private String address;
        private String postalCode;
        private String subzoneName;
        private Double latitude;
        private Double longitude;

        public LocationDto(Location location) {
            this.address = location.getAddress();
            this.postalCode = location.getPostalCode();
            this.subzoneName = location.getSubzone().getSubzoneName();
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }
    }

    private Long shiftId;
    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private WorkerDetailsDto workerDetails = null;
    private LocationDto clientLocation;
    private LocationDto workerLocationBeforeShift = null;
    private String sessionDescription;
    private LocalDate sessionStartDate;
    private LocalTime sessionStartTime;
    private LocalDate sessionEndDate;
    private LocalTime sessionEndTime;
    private LocalDate actualStartDate;
    private LocalTime actualStartTime;
    private LocalDate actualEndDate;
    private LocalTime actualEndTime;
    private CleaningSession.SessionStatus sessionStatus;

    public ShiftWithWorkerDetailAndTripDto(Shift shift, Location workerLastLocation, boolean workerHasPendingLeave) {
        this.shiftId = shift.getShiftId();
        this.clientName = shift.getCleaningSession().getClientSite().getClient().getName();
        this.clientPhone = shift.getCleaningSession().getClientSite().getClient().getPhone();
        this.clientAddress = shift.getCleaningSession().getClientSite().getLocation().getAddress();
        Worker worker = shift.getWorker();
        if (worker != null) {
            workerDetails = new WorkerDetailsDto(worker, workerHasPendingLeave);
        }
        this.clientLocation = new LocationDto(shift.getCleaningSession().getClientSite().getLocation());
        if (workerLastLocation != null) {
            this.workerLocationBeforeShift = new LocationDto(workerLastLocation);
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
        this.sessionStatus = shift.getCleaningSession().getSessionStatus();
    }
}
