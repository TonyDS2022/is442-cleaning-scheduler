package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CleaningSessionService {

    private final CleaningSessionRepository cleaningSessionRepository;
    private final ShiftRepository shiftRepository;
    private final LeaveApplicationService leaveApplicationService;

    @Autowired
    public CleaningSessionService(
            CleaningSessionRepository cleaningSessionRepository,
            ShiftRepository shiftRepository,
            LeaveApplicationService leaveApplicationService
    ) {
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.shiftRepository = shiftRepository;
        this.leaveApplicationService = leaveApplicationService;
    }

    public List<CleaningSession> getAllCleaningSessions() {
        List<CleaningSession> cleaningSessions = cleaningSessionRepository.findAll();
        cleaningSessions.forEach(session -> {
            updateWorkerHasPendingLeave(session);
            session.setPlanningStage(getPlanningStage(session));
            cleaningSessionRepository.save(session);
        });
        return cleaningSessions;
    }

    // get cleaningsessions by sessionid
    public Optional<CleaningSession> getCleaningSessionById(Long cleaningSessionId) {
        Optional<CleaningSession> cleaningSession = cleaningSessionRepository.findById(cleaningSessionId);
        cleaningSession.ifPresent(session -> {
            session.setPlanningStage(getPlanningStage(session));
            cleaningSessionRepository.save(session);
        });
        return cleaningSession;
    }

    public List<CleaningSession> getCleaningSessionsByContractId(Long contractId) {
        List<CleaningSession> cleaningSessions = cleaningSessionRepository.findByContract_ContractId(contractId);
        cleaningSessions.forEach(session -> {
            session.setPlanningStage(getPlanningStage(session));
            cleaningSessionRepository.save(session);
        });
        return cleaningSessions;
    }

    public Optional<CleaningSession> getCleaningSessionByContractIdAndCleaningSessionId(Long contractId, Long cleaningSessionId) {
        Optional<CleaningSession> cleaningSession = cleaningSessionRepository.findByContract_ContractIdAndCleaningSessionId(contractId, cleaningSessionId);
        cleaningSession.ifPresent(session -> {
            session.setPlanningStage(getPlanningStage(session));
            cleaningSessionRepository.save(session);
        });
        return cleaningSession;
    }

    // // try to calculate workerhaspendingleave dynamically
    @Transactional // get the changes are properly committed to the database.
    public void updateWorkerHasPendingLeave(CleaningSession cleaningSession) {
        for (Shift shift : cleaningSession.getShifts()) {
            if (shift.getWorker() != null) {
                boolean hasPendingLeave = leaveApplicationService.getPendingApplicationsByWorkerId(shift.getWorker().getWorkerId())
                        .stream()
                        .anyMatch(leave -> isOverlapping(leave.getLeaveStartDate(), leave.getLeaveStartDate(), shift));
                shift.setWorkerHasPendingLeave(hasPendingLeave);
                shiftRepository.save(shift);
            } else {
                shift.setWorkerHasPendingLeave(false);
                shiftRepository.save(shift);
            }
        }
        shiftRepository.flush(); // Ensure changes are flushed / persist to the database
    }

    private boolean isOverlapping(LocalDate leaveStart, LocalDate leaveEnd, Shift shift) {
        LocalDate shiftStartDate = shift.getSessionStartDate();
        LocalDate shiftEndDate = shift.getSessionEndDate();
        return (leaveStart.isBefore(shiftEndDate) || leaveStart.isEqual(shiftEndDate)) &&
                (leaveEnd.isAfter(shiftStartDate) || leaveEnd.isEqual(shiftStartDate));
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
}