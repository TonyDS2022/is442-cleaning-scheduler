package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;
    private final CleaningSessionRepository cleaningSessionRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository,
                        WorkerRepository workerRepository,
                        CleaningSessionRepository cleaningSessionRepository
    ) {
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
        this.cleaningSessionRepository = cleaningSessionRepository;
    }

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public Optional<Shift> getShiftById(Long shiftId) {
        return shiftRepository.findById(shiftId);
    }

    public void addShift(Shift shift) {
        return shiftRepository.save(shift);
        // updateCleaningSessionPlanningStage(shift.getCleaningSession());
    }

    public Shift updateShift(Long shiftId, Shift updatedShift) {
        if (!shiftRepository.existsById(shiftId)) {
            throw new IllegalArgumentException("Shift not found");
        }
        updatedShift.setShiftId(shiftId);
        return shiftRepository.save(updatedShift);
    }

    public void deactivateShift(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        Worker worker = shift.getWorker();
        if(worker != null){
            shift.setWorker(null);
        }
        shift.setActive(false);
        shiftRepository.save(shift);
    }

    public void deleteShift(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        shiftRepository.deleteById(shiftId);
        // updateCleaningSessionPlanningStage(shift.getCleaningSession());
    }

    // private void updateCleaningSessionPlanningStage(CleaningSession cleaningSession) {
    //     cleaningSession.updatePlanningStage();
    //     cleaningSessionRepository.save(cleaningSession);
    // }

    public List<Shift> getShiftsByWorkerId(Long workerId) {
        return shiftRepository.findByWorkerWorkerId(workerId);
    }

    public void setWorker(Long shiftId, Long workerId){
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));

        if (shift.getWorker() != null && shift.getWorker().equals(worker)) {
            throw new IllegalStateException("Worker is already assigned to this shift.");
        }

        if(shift.getSessionStartTime().isBefore(worker.getStartWorkingHours()) || shift.getSessionEndTime().isAfter(worker.getEndWorkingHours())){
            throw new IllegalStateException("The shift time falls outside of the worker's designated working hours. Please assign a shift that matches the worker's availability.");
        }

        shift.setWorker(worker);
        shiftRepository.save(shift);
    }
}