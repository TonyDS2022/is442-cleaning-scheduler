package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final ShiftRepository shiftRepository;


    @Autowired
    public WorkerService(WorkerRepository workerRepository, ShiftRepository shiftRepository) {
        this.workerRepository = workerRepository;
        this.shiftRepository = shiftRepository;
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker getWorkerByUsername(String username) {
        return workerRepository.findByUsername(username).orElse(null);
    }

    public ResponseEntity<String> addWorker(Worker worker){
        workerRepository.save(worker);
        return ResponseEntity.ok("Worker added");
    }

    public ResponseEntity<String> updateWorker(Long workerId, Worker updatedWorker){
        if(!workerRepository.existsById(workerId)){
            throw new IllegalArgumentException("Worker not found");
        }
        updatedWorker.setWorkerId(workerId);
        workerRepository.save(updatedWorker);
        return ResponseEntity.ok("Worker updated");
    }

    // soft delete - make worker inactive
    public ResponseEntity<String> deactivateWorker(Long workerId){
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));

        List<Shift> shifts = shiftRepository.findByWorkerWorkerId(workerId);

        // filter for shift that happen after date of deactivation
        List<Shift> futureShifts = shifts.stream()
                .filter(shift -> shift.getSessionStartDate()
                        .isAfter(LocalDate.now()))
                .toList();

        StringBuilder message = new StringBuilder();

        if (!futureShifts.isEmpty()) {
            // Remove worker from future shifts
            for (Shift shift: futureShifts){
                shift.setWorker(null);
                shiftRepository.save(shift);
            }

            // Append the IDs of the deleted shifts to the message
            message.append("Removed worker from future shifts with shift IDs: ")
                    .append(futureShifts.stream().map(Shift::getShiftId).collect(Collectors.toList()))
                    .append(". ");
        }
        worker.setIsActive(false);
        workerRepository.save(worker);
        message.append("Worker with ID ").append(workerId).append(" marked as inactive successfully.");
        return ResponseEntity.ok(message.toString());
    }
}
