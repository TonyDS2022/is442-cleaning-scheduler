package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

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