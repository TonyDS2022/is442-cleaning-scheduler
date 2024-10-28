package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository,
                        WorkerRepository workerRepository
    ) {
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
    }

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public Optional<Shift> getShiftById(Long shiftId) {
        return shiftRepository.findById(shiftId);
    }

    public void addShift(Shift shift) {
        shiftRepository.save(shift);
        // updateCleaningSessionPlanningStage(shift.getCleaningSession());
    }

    public void updateShift(Long shiftId, Shift shift) {
        if (shiftRepository.existsById(shiftId)) {
            shift.setShiftId(shiftId);
            shiftRepository.save(shift);
        }
    }

    // public void deleteShift(Long shiftId) {
    //     shiftRepository.deleteById(shiftId);
    // }
    public void deleteShift(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        shiftRepository.deleteById(shiftId);
        // updateCleaningSessionPlanningStage(shift.getCleaningSession());
    }

    // get shifts by month, week, day, worker
    public List<Shift> getShiftsByMonthAndWorker(int month, int year, Long workerId) {
        return shiftRepository.findByMonthAndWorker(month, year, workerId);
    }

    public List<Shift> getShiftsByWeekAndWorker(int week, int year, Long workerId) {
        return shiftRepository.findByWeekAndWorker(week, year, workerId);
    }

    public List<Shift> getShiftsByDayAndWorker(LocalDate date, Long workerId) {
        return shiftRepository.findByDayAndWorker(date, workerId);
    }

    public List<Shift> getLastShiftByDayAndWorkerBeforeTime(Long workerId, LocalDate date, LocalTime time) {
        return shiftRepository.findLastShiftByDayAndWorkerBeforeTime(workerId, date, time);
    }

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

    public boolean shiftsTimeOverlap(Shift existingShift, Shift newShift){
        // this encompasses all edge cases:
        // 1. partial overlap
        // 2. full overlap (new shift within existing shift/ existing shift within new shift)
        // 3. touching boundaries (new shift ends exactly when the existing shift starts etc)
        return !newShift.getSessionEndTime().isBefore(existingShift.getSessionStartTime())
                && !newShift.getSessionStartTime().isAfter(existingShift.getSessionEndTime());
    }

    public boolean shiftIsWithinWorkingHours(Worker worker, Shift newShift){
        return !newShift.getSessionStartTime().isBefore(worker.getStartWorkingHours())
                && !newShift.getSessionEndTime().isAfter(worker.getEndWorkingHours());
    }

    public boolean isWorkerAvailable(Worker worker, Shift newShift){
        List<Shift> workerShifts = this.getShiftsByDayAndWorker(newShift.getSessionStartDate(), worker.getWorkerId());
        boolean hasConflict = workerShifts.stream()
                .anyMatch(existingShift -> shiftsTimeOverlap(existingShift, newShift));

        if(hasConflict){
            return false;
        }

        return shiftIsWithinWorkingHours(worker,newShift);
    }

    public List<Worker> getAvailableWorkersForShift(Long shiftId) {
        Shift newShift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));

        List<Worker> allWorkers = workerRepository.findAll();

        return allWorkers.stream()
                .filter(worker -> isWorkerAvailable(worker, newShift))
                .collect(Collectors.toList());
    }
}