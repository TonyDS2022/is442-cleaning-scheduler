package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;
    private final LeaveApplicationService leaveApplicationService;
    private final CleaningSessionRepository cleaningSessionRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository,
                        WorkerRepository workerRepository,
                        LeaveApplicationService leaveApplicationService
                        CleaningSessionRepository cleaningSessionRepository
    ) {
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
        this.leaveApplicationService = leaveApplicationService;
        this.cleaningSessionRepository = cleaningSessionRepository;
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

    public boolean isWorkerAvailableForShift(Worker worker, Shift newShift){

        boolean isOnLeave = leaveApplicationService.isWorkerOnLeave(
                worker.getWorkerId(),
                newShift.getSessionStartDate(),
                newShift.getSessionStartTime(),
                newShift.getSessionEndDate(),
                newShift.getSessionEndTime()
        );

        if (isOnLeave) {
            return false;
        }

        List<Shift> workerShifts = this.getShiftsByDayAndWorker(newShift.getSessionStartDate(), worker.getWorkerId());
        boolean hasConflict = workerShifts.stream()
                .anyMatch(existingShift -> shiftsTimeOverlap(existingShift, newShift));

        if(hasConflict){
            return false;
        }

        return shiftIsWithinWorkingHours(worker,newShift);
    }

    public List<Worker> getAvailableWorkersForShift(Shift newShift) {
        List<Worker> allWorkers = workerRepository.findAll();
        return allWorkers.stream()
                .filter(worker -> isWorkerAvailableForShift(worker, newShift))
                .collect(Collectors.toList());
    }

    public Shift findLastShiftOnDayBeforeTime(Long workerId, LocalDate date, LocalTime time){
        List<Shift> allWorkerShiftsOnDay = this.getLastShiftByDayAndWorkerBeforeTime(workerId, date, time);
        List<Shift> allWorkerShiftsOnDayBeforeTime = allWorkerShiftsOnDay.stream()
                .filter(shift -> shift.getSessionEndTime().isBefore(time))
                .toList();

        if (allWorkerShiftsOnDayBeforeTime.isEmpty()) {
            return null;
        }

        return allWorkerShiftsOnDayBeforeTime.stream()
                .max((shift1, shift2) -> shift1.getSessionEndTime().compareTo(shift2.getSessionEndTime()))
                .orElse(null);
    }
}