package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.Trip;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public CleaningSessionService(
            CleaningSessionRepository cleaningSessionRepository,
            ShiftRepository shiftRepository,
            WorkerRepository workerRepository,
            TripRepository tripRepository
    ) {
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
        this.tripRepository = tripRepository;
    }

    public List<CleaningSession> getAllCleaningSessions() {
        return cleaningSessionRepository.findAll();
    }

    public List<CleaningSession> getCleaningSessionsByContractId(Long contractId) {
        return cleaningSessionRepository.findByContract_ContractId(contractId);
    }

    public Optional<CleaningSession> getCleaningSessionByContractIdAndCleaningSessionId(Long contractId, Long cleaningSessionId) {
        return cleaningSessionRepository.findByContract_ContractIdAndCleaningSessionId(contractId, cleaningSessionId);
    }

    public List<AvailableWorkerDto> getAssignableWorkers(Long cleaningSessionId) {
        CleaningSession session = cleaningSessionRepository.findById(cleaningSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));

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
            Shift lastShift = shiftRepository.findLastShiftOnDateByWorkerWorkerId(worker.getWorkerId(), session.getSessionStartDate());
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
}