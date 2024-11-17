package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.AvailableWorkerDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.Trip;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CleaningSessionService {

    private final CleaningSessionRepository cleaningSessionRepository;
    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;
    private final TripRepository tripRepository;
    private final ShiftService shiftService;
    private final WorkerService workerService;
    private final LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    public CleaningSessionService(
            CleaningSessionRepository cleaningSessionRepository,
            ShiftRepository shiftRepository,
            WorkerRepository workerRepository,
            TripRepository tripRepository,
            ShiftService shiftService,
            WorkerService workerService, LeaveApplicationRepository leaveApplicationRepository) {
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
        this.tripRepository = tripRepository;
        this.shiftService = shiftService;
        this.workerService = workerService;
        this.leaveApplicationRepository = leaveApplicationRepository;
    }

    public CleaningSession addCleaningSession(CleaningSession cleaningSession){
        return cleaningSessionRepository.save(cleaningSession);
    }

    public CleaningSession updateCleaningSession(Long cleaningSessionId, CleaningSessionUpdateDto updatedSessionDto){
        if(!cleaningSessionRepository.existsById(cleaningSessionId)){
            throw new IllegalArgumentException("Cleaning session not found");
        }
        CleaningSession existingSession = cleaningSessionRepository.findById(cleaningSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));

        if (updatedSessionDto.getSessionStartDate() != null) {
            existingSession.setSessionStartDate(updatedSessionDto.getSessionStartDate());
        }
        if (updatedSessionDto.getSessionStartDate() != null) {
            existingSession.setSessionStartDate(updatedSessionDto.getSessionStartDate());
        }
        if (updatedSessionDto.getSessionStartTime() != null) {
            existingSession.setSessionStartTime(updatedSessionDto.getSessionStartTime());
        }
        if (updatedSessionDto.getSessionEndDate() != null) {
            existingSession.setSessionEndDate(updatedSessionDto.getSessionEndDate());
        }
        if (updatedSessionDto.getSessionEndTime() != null) {
            existingSession.setSessionEndTime(updatedSessionDto.getSessionEndTime());
        }
        if (updatedSessionDto.getSessionDescription() != null) {
            existingSession.setSessionDescription(updatedSessionDto.getSessionDescription());
        }
        if (updatedSessionDto.getSessionRating() != null) {
            existingSession.setSessionRating(updatedSessionDto.getSessionRating());
        }
        if (updatedSessionDto.getPlanningStage() != null) {
            existingSession.setPlanningStage(updatedSessionDto.getPlanningStage());
        }
        if (updatedSessionDto.getSessionFeedback() != null) {
            existingSession.setSessionFeedback(updatedSessionDto.getSessionFeedback());
        }
        return cleaningSessionRepository.save(existingSession);
    }

    public void cancelCleaningSession(Long cleaningSessionId) {
        CleaningSession cleaningSession = cleaningSessionRepository.findById(cleaningSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));

        if(cleaningSession.getSessionStatus() == CleaningSession.SessionStatus.CANCELLED){
            throw new IllegalArgumentException("Cleaning session has already been cancelled");
        }
        else if(cleaningSession.getSessionStatus() == CleaningSession.SessionStatus.WORKING){
            throw new IllegalArgumentException("Cleaning session cannot be cancelled as it is ongoing");
        }
        else if(cleaningSession.getSessionStatus() == CleaningSession.SessionStatus.FINISHED){
            throw new IllegalArgumentException("Cleaning session cannot be cancelled because it has already been finished.");
        }
        // sessionStatus == NOT_STARTED
        else{
            cleaningSession.setSessionStatus(CleaningSession.SessionStatus.CANCELLED);
            cleaningSession.setCancelledAt(LocalDate.now());
            // cancel linked shift that has not occurred yet
            List<Shift> shifts = shiftRepository.findByCleaningSession_CleaningSessionId(cleaningSessionId);
            if(!shifts.isEmpty()){
                for(Shift shift : shifts) {
                    // cancel linked shifts has not occurred yet
                    if (shift.getSessionStartDate().isAfter(LocalDate.now())) {
                        Long shiftId = shift.getShiftId();
                        shiftService.cancelShift(shiftId);
                    }
                }
            }
            cleaningSessionRepository.save(cleaningSession);
        }
    }

    public SessionReportDto getMonthlySessionReport(int year, int month) {
        LocalDate startOfMonth = YearMonth.of(year, month).atDay(1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        Long numFinishedSessions = cleaningSessionRepository.countNoOfMonthFinishedSessions(startOfMonth, endOfMonth);
        Long numCancelledSessions = cleaningSessionRepository.countNoOfMonthCancelledSessions(startOfMonth, endOfMonth);

        return new SessionReportDto(numFinishedSessions, numCancelledSessions);
    }

    // // try to calculate workerhaspendingleave dynamically
    // @Transactional // get the changes are properly committed to the database.
    public void updateWorkerHasPendingLeave(CleaningSession cleaningSession) {
        for (Shift shift : cleaningSession.getShifts()) {
            if (shift.getWorker() != null) {
                boolean hasPendingLeave = workerService.workerHasPendingOrApprovedLeaveBetween(
                        shift.getWorker().getWorkerId(),
                        shift.getSessionStartDate(),
                        shift.getSessionEndDate()
                );
                shift.setWorkerHasPendingLeave(hasPendingLeave);
                shiftRepository.save(shift);
            } else {
                shift.setWorkerHasPendingLeave(false);
                shiftRepository.save(shift);
            }
        }
        // shiftRepository.flush(); // Ensure changes are flushed / persist to the database
    }

    public CleaningSession.PlanningStage getPlanningStage(CleaningSession cleaningSession) {
        updateWorkerHasPendingLeave(cleaningSession);

        boolean hasPendingLeave = false;
        int assignedWorkers = 0;

        for (Shift shift : cleaningSession.getShifts()) {
            if (shift.isWorkerHasPendingLeave()) {
                hasPendingLeave = true;
            }
            if (shift.getWorker() != null) {
                assignedWorkers++;
            }
        }

        if (assignedWorkers < cleaningSession.getWorkersBudgeted()) {
            return CleaningSession.PlanningStage.RED;
        } else if (hasPendingLeave) {
            return CleaningSession.PlanningStage.EMBER;
        } else {
            return CleaningSession.PlanningStage.GREEN;
        }
    }

    public SessionReportDto getYearlySessionReport(int year) {
        Long numFinishedSessions = cleaningSessionRepository.countNoOfYearFinishedSessions(year);
        Long numCancelledSessions = cleaningSessionRepository.countNoOfYearCancelledSessions(year);

        return new SessionReportDto(numFinishedSessions, numCancelledSessions);
    }

    public List<CleaningSessionCalendarViewDto> getCalendarView() {
        List<CleaningSession> cleaningSessions = cleaningSessionRepository.findAll();
        return cleaningSessions.stream()
                .map(CleaningSessionCalendarViewDto::new)
                .toList();
    }

    public CleaningSessionCalendarCardViewDto getCalendarCardView(Long cleaningSessionId) {
        CleaningSession cleaningSession = cleaningSessionRepository.findById(cleaningSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));
        LocalDate sessionStartDate = cleaningSession.getSessionStartDate();
        LocalDate sessionEndDate = cleaningSession.getSessionEndDate();

        List<Long> workerIds = cleaningSession.getShifts().stream()
                .map(shift -> shift.getWorker() != null ? shift.getWorker().getWorkerId() : null)
                .filter(Objects::nonNull) // Filter out null worker IDs to avoid null pointer exceptions
                .toList();

        Map<Long, Boolean> workerLeaveStatusMap = workerIds.stream()
                .collect(Collectors.toMap(
                        workerId -> workerId,
                        workerId -> leaveApplicationRepository.existsByWorkerAndStatusAndDateRangeOverlapping(
                                workerId,
                                LeaveApplication.ApplicationStatus.PENDING,
                                sessionStartDate,
                                sessionEndDate
                        )
                ));

        return new CleaningSessionCalendarCardViewDto(cleaningSession, workerLeaveStatusMap);
    }
}