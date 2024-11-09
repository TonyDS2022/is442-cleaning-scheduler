package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void cancelShift(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        Worker worker = shift.getWorker();
        if(worker != null){
            shift.setWorker(null);
        }
        if(shift.getWorkingStatus() == Shift.WorkingStatus.CANCELLED){
            throw new IllegalArgumentException("Shift has already been cancelled");
        }
        else if(shift.getWorkingStatus() == Shift.WorkingStatus.WORKING){
            throw new IllegalArgumentException("Shift cannot be cancelled as it is ongoing");
        }
        else if(shift.getWorkingStatus() == Shift.WorkingStatus.FINISHED){
            throw new IllegalArgumentException("Shift cannot be cancelled because it has already been finished.");
        }
        // workingStatus == NOT_STARTED
        else{
            shift.setWorkingStatus(Shift.WorkingStatus.CANCELLED);
            shift.setCancelledAt(LocalDate.now());
            shiftRepository.save(shift);
        }
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

    public WorkerHoursDto getYearlyHoursOfWorker(Long workerId, int year){
        Long totalHours = shiftRepository.getWorkerTotalHoursWorkedInYear(workerId, year);
        long totalOverTimeHours = calculateYearlyOverTime(workerId, year);
        return new WorkerHoursDto(totalHours, totalOverTimeHours);
    }

    public WorkerHoursDto getMonthlyHoursOfWorker(Long workerId, int year, int month){
        Long totalHours = shiftRepository.getWorkerTotalHoursWorkedInMonth(workerId, year, month);
        Long totalOverTimeHours = calculateMonthlyOverTime(workerId, year, month);
        return new WorkerHoursDto(totalHours, totalOverTimeHours);
    }

    public WorkerHoursDto getWeeklyHoursOfWorker(Long workerId, LocalDate startOfWeek, LocalDate endOfWeek){
        List<Long> weeklyHours =  shiftRepository.getWorkerTotalHoursWorkedInWeek(workerId, startOfWeek, endOfWeek);
        long totalHours = weeklyHours.stream().mapToLong(Long::longValue).sum();
        long overTimeHours = Math.max(0, totalHours - ShiftConfigLoader.WEEKLY_OVERTIME_THRESHOLD_HOURS);
        return new WorkerHoursDto(totalHours, overTimeHours);
    }

    public Long calculateYearlyOverTime(Long workerId, int year){
        long totalOvertimeHours = 0;
        LocalDate startOfWeek = LocalDate.of(year, 1, 1);
        LocalDate endOfWeek = startOfWeek.plusDays(7);

        while (startOfWeek.getYear() == year) {
            List<Long> weeklyHours = shiftRepository.getWorkerTotalHoursWorkedInWeek(workerId, startOfWeek, endOfWeek);
            long totalHours = weeklyHours.stream().mapToLong(Long::longValue).sum();
            totalOvertimeHours += Math.max(0, totalHours - ShiftConfigLoader.WEEKLY_OVERTIME_THRESHOLD_HOURS);
            startOfWeek = endOfWeek;
            endOfWeek = startOfWeek.plusDays(7);
        }

        return totalOvertimeHours;
    }

    public Long calculateMonthlyOverTime(Long workerId, int year, int month){
        long totalOvertimeHours = 0;
        LocalDate startOfWeek = LocalDate.of(year, month, 1);
        LocalDate endOfWeek = startOfWeek.plusDays(7);

        while (startOfWeek.getMonthValue() == month) {
            List<Long> weeklyHours = shiftRepository.getWorkerTotalHoursWorkedInWeek(workerId, startOfWeek, endOfWeek);
            long totalHours = weeklyHours.stream().mapToLong(Long::longValue).sum();
            totalOvertimeHours += Math.max(0, totalHours - ShiftConfigLoader.WEEKLY_OVERTIME_THRESHOLD_HOURS);
            startOfWeek = endOfWeek;
            endOfWeek = startOfWeek.plusDays(7);
        }

        return totalOvertimeHours;
    }
}