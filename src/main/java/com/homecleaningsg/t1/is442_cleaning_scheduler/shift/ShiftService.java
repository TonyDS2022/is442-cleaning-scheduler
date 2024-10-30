package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;
    private final LeaveApplicationService leaveApplicationService;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository,
                        WorkerRepository workerRepository,
                        LeaveApplicationService leaveApplicationService
    ) {
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
        this.leaveApplicationService = leaveApplicationService;
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

    public boolean isWorkerHasPendingLeave(Shift shift) {
        return true; // for debugging. Revert comments below to get code working
        // if (shift.getWorker() == null) {
        //     return false;
        // }
        // List<LeaveApplication> leaveApplications = leaveApplicationService.getPendingApplicationsByWorkerId(shift.getWorker().getWorkerId());
        // for (LeaveApplication leaveApplication : leaveApplications) {
        //     return true;
        //     // if (isOverlapping(shift, leaveApplication.getAffectedShiftStart(), leaveApplication.getAffectedShiftEnd())) {
        //     //     return true;
        //     // }
        // }
        // return false;
    }

    private boolean isOverlapping(Shift shift, OffsetDateTime leaveStart, OffsetDateTime leaveEnd) {
        return true;
        // OffsetDateTime shiftStart = shift.getSessionStartDate().atTime(shift.getSessionStartTime()).atOffset(OffsetDateTime.now().getOffset());
        // OffsetDateTime shiftEnd = shift.getSessionEndDate().atTime(shift.getSessionEndTime()).atOffset(OffsetDateTime.now().getOffset());
        // return (leaveStart.isBefore(shiftEnd) && leaveEnd.isAfter(shiftStart));
    }
}