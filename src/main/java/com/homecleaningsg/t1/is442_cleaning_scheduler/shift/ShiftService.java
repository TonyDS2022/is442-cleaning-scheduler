package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.Trip;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final WorkerRepository workerRepository;
    private final CleaningSessionRepository cleaningSessionRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final TripRepository tripRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository,
                        WorkerRepository workerRepository,
                        CleaningSessionRepository cleaningSessionRepository,
                        LeaveApplicationRepository leaveApplicationRepository,
                        TripRepository tripRepository
    ) {
        this.shiftRepository = shiftRepository;
        this.workerRepository = workerRepository;
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.tripRepository = tripRepository;
    }

    public List<ShiftWithWorkerDetailsDto> getAllShifts() {

        List<Shift> shifts = shiftRepository.findAll();

        List<ShiftWithWorkerDetailsDto> shiftWithWorkerDetailsDtos = new ArrayList<>();

        for (Shift shift : shifts) {
            Worker worker = shift.getWorker();
            ShiftWithWorkerDetailsDto shiftWithWorkerDetailsDto = new ShiftWithWorkerDetailsDto(shift);
            shiftWithWorkerDetailsDtos.add(shiftWithWorkerDetailsDto);
        }

        return shiftWithWorkerDetailsDtos;
    }

    public Optional<ShiftWithWorkerDetailsDto> getShiftById(Long shiftId) {
        Optional<Shift> shift = shiftRepository.findById(shiftId);
        if (shift.isEmpty()) {
            return Optional.empty();
        }
        ShiftWithWorkerDetailsDto shiftWithWorkerDetailsDto = new ShiftWithWorkerDetailsDto(shift.get());
        return Optional.of(shiftWithWorkerDetailsDto);
    }

    public void addShift(Shift shift) {
        // attempt to assign worker
        List<AvailableWorkerDto> availableWorkers = getAvailableWorkersForShift(shift);
        if (!availableWorkers.isEmpty()) {
            Worker worker = workerRepository.findById(availableWorkers.get(0).getWorkerId())
                    .orElseThrow(() -> new IllegalArgumentException("Worker not found"));
            setWorker(shift, worker);
        }
        shiftRepository.save(shift);
        // updateCleaningSessionPlanningStage(shift.getCleaningSession());
    }

    public void updateShift(Long shiftId, Map<String, String> updates) {
        Shift existingShift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        CleaningSession existingCleaningSession = existingShift.getCleaningSession();

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "sessionStartDate":
                        LocalDate startDate = LocalDate.parse(value);
                        existingCleaningSession.setSessionStartDate(startDate);
                        existingShift.setSessionStartDate(startDate);
                        break;

                    case "sessionStartTime":
                        LocalTime startTime = LocalTime.parse(value);
                        existingCleaningSession.setSessionStartTime(startTime);
                        existingShift.setSessionStartTime(startTime);
                        break;

                    case "sessionEndDate":
                        LocalDate endDate = LocalDate.parse(value);
                        existingCleaningSession.setSessionEndDate(endDate);
                        existingShift.setSessionEndDate(endDate);
                        break;

                    case "sessionEndTime":
                        LocalTime endTime = LocalTime.parse(value);
                        existingCleaningSession.setSessionEndTime(endTime);
                        existingShift.setSessionEndTime(endTime);
                        break;

                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date or time format for key: " + key);
            }
        });

        // Save entities once after all updates are applied
        cleaningSessionRepository.save(existingCleaningSession);
        shiftRepository.save(existingShift);
    }

    public void unassignWorkerFromShift(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        if (shift.getWorkingStatus() != Shift.WorkingStatus.NOT_STARTED) {
            throw new IllegalArgumentException("Past shifts cannot be unassigned.");
        }
        shiftRepository.save(shift);
    }

    public void updateCleaningSessionStatus(CleaningSession cleaningSession) {
        List<Shift> shifts = cleaningSession.getShifts();

        boolean anyFinished = false;
        boolean anyWorking = false;

        for (Shift shift : shifts) {
            Shift.WorkingStatus shiftStatus = shift.getWorkingStatus();
            if (shiftStatus == Shift.WorkingStatus.FINISHED) {
                anyFinished = true;
            }
            if (shiftStatus == Shift.WorkingStatus.WORKING) {
                anyWorking = true;
            }
        }

        if (anyFinished) {
            cleaningSession.setSessionStatus(CleaningSession.SessionStatus.FINISHED);
        } else if (anyWorking) {
            cleaningSession.setSessionStatus(CleaningSession.SessionStatus.WORKING);
        }
        // Save the updated cleaning session status
        cleaningSessionRepository.save(cleaningSession);
    }

    public void startShift(long shiftId) {
        // TBC: what do we do with the other shifts in the same cleaning session?
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        if(shift.getWorkingStatus() == Shift.WorkingStatus.WORKING){
            throw new IllegalArgumentException("Shift has already started");
        }
        else if(shift.getWorkingStatus() == Shift.WorkingStatus.FINISHED){
            throw new IllegalArgumentException("Shift has already finished");
        }
        else if(shift.getWorkingStatus() == Shift.WorkingStatus.CANCELLED){
            throw new IllegalArgumentException("Shift has been cancelled");
        }
        else if(shift.getWorker() == null) {
            throw new IllegalArgumentException("Shift has no worker assigned");
        }
        else{
            shift.setWorkingStatus(Shift.WorkingStatus.WORKING);
            shift.setActualStartDate(LocalDate.now());
            shift.setActualStartTime(LocalTime.now().withNano(0));
            shiftRepository.save(shift);
        }
        CleaningSession cleaningSession = shift.getCleaningSession();
        // update cleaning status status
        updateCleaningSessionStatus(cleaningSession);
    }

    public void endShift(long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        if(shift.getWorkingStatus() == Shift.WorkingStatus.NOT_STARTED){
            throw new IllegalArgumentException("Shift has not started");
        }
        else if(shift.getWorkingStatus() == Shift.WorkingStatus.FINISHED){
            throw new IllegalArgumentException("Shift has already finished");
        }
        else if(shift.getWorkingStatus() == Shift.WorkingStatus.CANCELLED){
            throw new IllegalArgumentException("Shift has been cancelled");
        }
        else if(shift.getWorker() == null) {
            throw new IllegalArgumentException("Shift has no worker assigned");
        }
        else{
            shift.setWorkingStatus(Shift.WorkingStatus.FINISHED);
            shift.setActualEndDate(LocalDate.now());
            shift.setActualEndTime(LocalTime.now().withNano(0));
            shiftRepository.save(shift);
        }
        CleaningSession cleaningSession = shift.getCleaningSession();
        // update cleaning status status
        updateCleaningSessionStatus(cleaningSession);
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
    public List<ShiftWithWorkerDetailsDto> getShiftsByMonthAndWorker(int month, int year, Long workerId) {
        List<Shift> shifts = shiftRepository.findByMonthAndWorker(month, year, workerId);
        List<ShiftWithWorkerDetailsDto> shiftWithWorkerDetailsDtos = new ArrayList<>();
        for (Shift shift : shifts) {
            shiftWithWorkerDetailsDtos.add(new ShiftWithWorkerDetailsDto(shift));
        }
        return shiftWithWorkerDetailsDtos;
    }

    public List<ShiftWithWorkerDetailsDto> getShiftsByWeekAndWorker(int week, int year, Long workerId) {
        List<Shift> shifts = shiftRepository.findByWeekAndWorker(week, year, workerId);
        List<ShiftWithWorkerDetailsDto> shiftWithWorkerDetailsDtos = new ArrayList<>();
        for (Shift shift : shifts) {
            shiftWithWorkerDetailsDtos.add(new ShiftWithWorkerDetailsDto(shift));
        }
        return shiftWithWorkerDetailsDtos;
    }

    public List<ShiftWithWorkerDetailsDto> getShiftsByDayAndWorker(LocalDate date, Long workerId) {
        List<Shift> shifts = shiftRepository.findByDayAndWorker(date, workerId);
        List<ShiftWithWorkerDetailsDto> shiftWithWorkerDetailsDtos = new ArrayList<>();
        for (Shift shift : shifts) {
            shiftWithWorkerDetailsDtos.add(new ShiftWithWorkerDetailsDto(shift));
        }
        return shiftWithWorkerDetailsDtos;
    }

    public List<ShiftWithWorkerDetailsDto> getLastShiftByDayAndWorkerBeforeTime(Long workerId, LocalDate date, LocalTime time) {
        List<Shift> shifts = shiftRepository.findLastShiftByDayAndWorkerBeforeTime(workerId, date, time);
        List<ShiftWithWorkerDetailsDto> shiftWithWorkerDetailsDtos = new ArrayList<>();
        for (Shift shift : shifts) {
            shiftWithWorkerDetailsDtos.add(new ShiftWithWorkerDetailsDto(shift));
        }
        return shiftWithWorkerDetailsDtos;
    }

    public List<ShiftWithWorkerDetailsDto> getShiftsDtosByWorkerId(Long workerId) {
        List<Shift> shifts = shiftRepository.findByWorkerWorkerId(workerId);
        List<ShiftWithWorkerDetailsDto> shiftWithWorkerDetailsDtos = new ArrayList<>();
        for (Shift shift : shifts) {
            shiftWithWorkerDetailsDtos.add(new ShiftWithWorkerDetailsDto(shift));
        }
        return shiftWithWorkerDetailsDtos;
    }

    public List<Shift> getShiftsByWorkerId(Long workerId) {
        return shiftRepository.findByWorkerWorkerId(workerId);
    }

    public void setWorker(Long shiftId, Long workerId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));

        setWorker(shift, worker);
    }

    public void setWorker(Shift shift, Worker worker) {
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
        return getAvailableWorkersForShift(shift);
    }

    public List<AvailableWorkerDto> getAvailableWorkersForShift(Shift shift) {
        LocalTime shiftStartTime = shift.getSessionStartTime();
        LocalTime shiftEndTime = shift.getSessionEndTime();

        List<Worker> allWorkersInWorkingHours = workerRepository.findByStartWorkingHoursBeforeEndWorkingHoursAfter(shiftStartTime, shiftEndTime);

        List<Worker> workersWithPendingOrApprovedLeave = leaveApplicationRepository.findWorkersByLeaveOverlappingWith(shift.getSessionStartDate(), shift.getSessionEndDate());

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
                .filter(worker -> !workersWithPendingOrApprovedLeave.contains(worker))
                .toList();

        Map<Worker, Shift> lastShiftByWorkerBeforeShift = new HashMap<>();
        for (Worker worker : availableWorkers) {
            Shift lastShift = shiftRepository.findLastShiftOnDateByWorkerWorkerIdAndSessionEndTimeBefore(worker.getWorkerId(), shift.getSessionStartDate(), shiftStartTime);
            lastShiftByWorkerBeforeShift.put(worker, lastShift);
        }

        Map<Worker, Location> workerOrigins = new HashMap<>();
        for (Worker worker : availableWorkers) {
            Shift lastShift = lastShiftByWorkerBeforeShift.get(worker);
            Location origin = (lastShift != null) ? lastShift.getClientSite().getLocation() : worker.getHomeLocation();
            workerOrigins.put(worker, origin);
        }

        Map<Worker, Trip> workerTrip = new HashMap<>();
        for (Worker worker : availableWorkers) {
            Location origin = workerOrigins.get(worker);
            Location destination = shift.getClientSite().getLocation();
            Trip trip = tripRepository.findTripByOriginAndDestination(origin, destination);
            workerTrip.put(worker, trip);
        }

        List<AvailableWorkerDto> results = availableWorkers.stream()
                .map(worker -> new AvailableWorkerDto(
                        worker.getWorkerId(),
                        worker.getName(),
                        workerOrigins.get(worker).getAddress(),
                        shift.getClientSite().getStreetAddress(),
                        workerTrip.get(worker).getTripDurationSeconds(),
                        workerTrip.get(worker).getTripDistanceMeters())
                ).toList();

        // Sort by ascending trip duration
        List<AvailableWorkerDto> mutableResults = new ArrayList<>(results);
        mutableResults.sort(Comparator.comparing(AvailableWorkerDto::getTripDurationSeconds));

        return mutableResults;
    }
}