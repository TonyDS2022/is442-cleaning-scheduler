package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository,
                        WorkerRepository workerRepository) {
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
    }

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public Optional<Shift> getShiftById(Long shiftId) {
        return shiftRepository.findById(shiftId);
    }

    public Shift addShift(Shift shift) {
        return shiftRepository.save(shift);
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

    public Long getYearlyHoursOfWorker(Long workerId, int year){
        return shiftRepository.getWorkerTotalHoursWorkedInYear(workerId, year);
    }

    public Long getMonthlyHoursOfWorker(Long workerId, int year, int month){
        return shiftRepository.getWorkerTotalHoursWorkedInMonth(workerId, year, month);
    }

    public Long getWeeklyHoursOfWorker(Long workerId, LocalDate startOfWeek, LocalDate endOfWeek){
        return shiftRepository.getWorkerTotalHoursWorkedInWeek(workerId, startOfWeek, endOfWeek);
    }
}