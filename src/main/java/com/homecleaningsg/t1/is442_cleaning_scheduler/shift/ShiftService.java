package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.Trip;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;
    private final LeaveApplicationService leaveApplicationService;
    private final CleaningSessionRepository cleaningSessionRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final TripRepository tripRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository,
                        WorkerRepository workerRepository,
                        LeaveApplicationService leaveApplicationService,
                        CleaningSessionRepository cleaningSessionRepository,
                        LeaveApplicationRepository leaveApplicationRepository,
                        TripRepository tripRepository
    ) {
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
        this.leaveApplicationService = leaveApplicationService;
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.tripRepository = tripRepository;
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

    public WorkerHoursDto getYearlyHoursOfWorker(Long workerId, int year){
        Long totalHours = shiftRepository.getWorkerTotalHoursWorkedInYear(workerId, year);
        if (totalHours == null) {
            totalHours = 0L;
        }
        long totalOverTimeHours = calculateYearlyOverTime(workerId, year);
        return new WorkerHoursDto(workerId, totalHours, totalOverTimeHours);
    }

    public WorkerHoursDto getMonthlyHoursOfWorker(Long workerId, int year, int month){
        Long totalHours = shiftRepository.getWorkerTotalHoursWorkedInMonth(workerId, year, month);
        if (totalHours == null) {
            totalHours = 0L;
        }
        Long totalOverTimeHours = calculateMonthlyOverTime(workerId, year, month);
        return new WorkerHoursDto(workerId, totalHours, totalOverTimeHours);
    }

    public WorkerHoursDto getWeeklyHoursOfWorker(Long workerId, LocalDate startOfWeek, LocalDate endOfWeek){
        List<Long> weeklyHours =  shiftRepository.getWorkerTotalHoursWorkedInWeek(workerId, startOfWeek, endOfWeek);
        long totalHours = weeklyHours.stream().mapToLong(Long::longValue).sum();
        long overTimeHours = Math.max(0, totalHours - ShiftConfigLoader.WEEKLY_OVERTIME_THRESHOLD_HOURS);
        return new WorkerHoursDto(workerId, totalHours, overTimeHours);
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

    public List<AvailableWorkerDto> getAvailableWorkersForShift(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));

        LocalTime shiftStartTime = shift.getSessionStartTime();
        LocalTime shiftEndTime = shift.getSessionEndTime();

        List<Worker> allWorkersInWorkingHours = workerRepository.findByStartWorkingHoursAfterAndEndWorkingHoursBefore(shiftStartTime, shiftEndTime);
//        List<Worker> workersWithPendingLeave = /* #TODO: get workers with pending leave applications */
//        List<Worker> workersWithApprovedLeave = /* #TODO: get workers with approved leave applications */

        List<Worker> workerOnShiftsWithStartTimeOverlaps = shiftRepository.findBySessionStartTimeBetween(shiftStartTime, shiftEndTime)
                .stream()
                .map(Shift::getWorker)
                .toList();
        List<Worker> workerOnShiftsWithEndTimeOverlaps = shiftRepository.findBySessionEndTimeBetween(shiftStartTime, shiftEndTime)
                .stream()
                .map(Shift::getWorker)
                .toList();
        Set<Worker> workerOnShift = new HashSet<>();
        workerOnShift.addAll(workerOnShiftsWithStartTimeOverlaps);
        workerOnShift.addAll(workerOnShiftsWithEndTimeOverlaps);

        List<Worker> availableWorkers = allWorkersInWorkingHours.stream()
                .filter(worker -> !workerOnShift.contains(worker))
//                .filter(worker -> !workersWithPendingLeave.contains(worker)) /* #TODO: uncomment when implemented */
//                .filter(worker -> !workersWithApprovedLeave.contains(worker)) /* #TODO: uncomment when implemented */
                .toList();

        Map<Worker, Shift> lastShiftByWorkerBeforeShift = new HashMap<>();
        for (Worker worker : availableWorkers) {
            Shift lastShift = shiftRepository.findLastShiftOnDateByWorkerWorkerIdAndSessionEndTimeBefore(worker.getWorkerId(), shift.getSessionStartDate(), shiftStartTime);
            lastShiftByWorkerBeforeShift.put(worker, lastShift);
        }

        Map<Worker, Location> workerOrigins = new HashMap<>();
        for (Worker worker : availableWorkers) {
            Shift lastShift = lastShiftByWorkerBeforeShift.get(worker);
            Location origin = (lastShift != null) ? lastShift.getLocation() : worker.getHomeLocation();
            workerOrigins.put(worker, origin);
        }

        Map<Worker, Trip> workerTrip = new HashMap<>();
        for (Worker worker : availableWorkers) {
            Location origin = workerOrigins.get(worker);
            Location destination = shift.getLocation();
            Trip trip = tripRepository.findTripByOriginAndDestination(origin, destination);
            workerTrip.put(worker, trip);
        }

        return availableWorkers.stream()
                .map(worker -> new AvailableWorkerDto(
                        worker.getWorkerId(),
                        worker.getName(),
                        workerOrigins.get(worker).getAddress(),
                        shift.getLocation().getAddress(),
                        workerTrip.get(worker).getTripDurationSeconds(),
                        workerTrip.get(worker).getTripDistanceMeters())
                ).toList();
    }
}