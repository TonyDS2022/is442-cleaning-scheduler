package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.AvailableWorkerDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.Trip;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CleaningSessionService {

    private final CleaningSessionRepository cleaningSessionRepository;
    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;
    private final TripRepository tripRepository;
    private final LeaveApplicationService leaveApplicationService;

    @Autowired
    public CleaningSessionService(
            CleaningSessionRepository cleaningSessionRepository,
            ShiftRepository shiftRepository,
            WorkerRepository workerRepository,
            TripRepository tripRepository,
            LeaveApplicationService leaveApplicationService
    ) {
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
        this.tripRepository = tripRepository;
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

    public CleaningSession addCleaningSession(CleaningSession cleaningSession){
        Contract contract = cleaningSession.getContract();
        Long durationMinutes = Duration.between(
                cleaningSession.getSessionStartTime(),
                cleaningSession.getSessionEndTime()
        ).toMinutes();


        if (contract.getSessionDurationMinutes() != durationMinutes) {
            throw new IllegalArgumentException("Cleaning session duration must be same as duration set in contract.");
        }

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
        if (updatedSessionDto.getSessionStatus() != null) {
            existingSession.setSessionStatus(updatedSessionDto.getSessionStatus());
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

    public void deactivateCleaningSession(Long cleaningSessionId) {
        CleaningSession cleaningSession = cleaningSessionRepository.findById(cleaningSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));
        cleaningSession.setActive(false);
        cleaningSessionRepository.save(cleaningSession);
    }

    public List<AvailableWorkerDto> getAssignableWorkers(Long cleaningSessionId) {
        CleaningSession session = cleaningSessionRepository.findById(cleaningSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));
        session.setPlanningStage(getPlanningStage(session));

        List<Worker> unavailableWorkers = shiftRepository
                .findBySessionStartTimeBetween(session.getSessionStartTime(), session.getSessionEndTime())
                .stream()
                .map(Shift::getWorker)
                .toList();

        List<Worker> allWorkers = workerRepository.findAll();

        List<Worker> availableWorkers = allWorkers.stream()
                .filter(worker -> !unavailableWorkers.contains(worker))
                .toList();

        Map<Worker, Shift> lastShiftByWorker = new HashMap<>();
        for (Worker worker : availableWorkers) {
            // Shift lastShift = shiftRepository.findLastShiftOnDateByWorkerWorkerId(worker.getWorkerId(), session.getSessionStartDate());
            Shift lastShift = shiftRepository.findLastShiftOnDateByWorkerWorkerIdAndSessionEndTimeBefore(
                    worker.getWorkerId(),
                    session.getSessionStartDate(),
                    session.getSessionEndTime()
            );
            lastShiftByWorker.put(worker, lastShift);
        }

        Map<Worker, Location> workerOrigins = new HashMap<>();
        for (Worker worker : availableWorkers) {
            Shift lastShift = lastShiftByWorker.get(worker);
            Location origin = (lastShift != null) ? lastShift.getLocation() : worker.getHomeLocation();
            workerOrigins.put(worker, origin);
        }

        Map<Worker, Trip> workerTrip = new HashMap<>();
        for (Worker worker : availableWorkers) {
            Location origin = workerOrigins.get(worker);
            Location destination = session.getLocation();
            Trip trip = tripRepository.findTripByOriginAndDestination(origin, destination);
            workerTrip.put(worker, trip);
        }

        return availableWorkers.stream()
                .map(worker -> new AvailableWorkerDto(
                        worker.getWorkerId(),
                        worker.getName(),
                        workerOrigins.get(worker).getAddress(),
                        session.getLocation().getAddress(),
                        workerTrip.get(worker).getTripDurationSeconds(),
                        workerTrip.get(worker).getTripDistanceMeters())
                ).toList();
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