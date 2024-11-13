package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final LocationRepository locationRepository;
    private final ShiftService shiftService;
    private final ShiftRepository shiftRepository;
    private final LocationService locationService;

    @Autowired
    public WorkerService(
            WorkerRepository workerRepository,
            LocationRepository locationRepository,
            ShiftService shiftService,
            ShiftRepository shiftRepository,
            LocationService locationService) {
        this.workerRepository = workerRepository;
        this.locationRepository = locationRepository;
        this.shiftService = shiftService;
        this.shiftRepository = shiftRepository;
        this.locationService = locationService;
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker getWorkerByUsername(String username) {
        return workerRepository.findByUsername(username).orElse(null);
    }

    public void addResidentialAddressToWorker(
            Long workerId,
            String streetAddress,
            String postalCode,
            String unitNumber
    ) {
        // Find the worker by ID
        Optional<Worker> workerOptional = workerRepository.findById(workerId);
        if (workerOptional.isEmpty()) {
            throw new RuntimeException("Worker not found");
        }
        Worker worker = workerOptional.get();
        addResidentialAddressToWorker(worker, streetAddress, postalCode, unitNumber);
    }

    public void addResidentialAddressToWorker(
            Worker worker,
            String streetAddress,
            String postalCode,
            String unitNumber
    ) {
        worker.setHomeUnitNumber(unitNumber);
        Location location = locationService.getOrCreateLocation(postalCode, streetAddress);
        worker.setHomeLocation(location);
        workerRepository.save(worker);
    }

    public Location getResidentialAddressOfWorker(Long workerId) {
        return workerRepository.findById(workerId)
                .map(Worker::getHomeLocation)
                .orElse(null); // Return null if worker not found
    }

    public Worker addWorker(Worker worker){
        worker.setJoinDate(LocalDate.now());
        return workerRepository.save(worker);
    }

    public Worker updateWorker(Long workerId, Worker updatedWorker){
        if(!workerRepository.existsById(workerId)){
            throw new IllegalArgumentException("Worker not found");
        }
        updatedWorker.setWorkerId(workerId);
        return workerRepository.save(updatedWorker);
    }

    public void deactivateWorker(Long workerId){
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));
        List<Shift> shifts = shiftRepository.findByWorkerWorkerId(workerId);

        // filter for shift that happen after date of deactivation
        List<Shift> futureShifts = shifts.stream()
                .filter(shift -> shift.getSessionStartDate()
                        .isAfter(LocalDate.now()))
                .toList();

        if (!futureShifts.isEmpty()) {
            // Remove worker from future shifts
            for (Shift shift : futureShifts) {
                shift.setWorker(null);
                shiftRepository.save(shift);
            }
        }

        worker.setActive(false);
        worker.setDeactivatedAt(LocalDate.now());
        workerRepository.save(worker);
    }

    public WorkerReportDto getMonthlyWorkerReport(int year, int month) {
        LocalDate startOfMonth = YearMonth.of(year, month).atDay(1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        Long newWorkers = workerRepository.countNewWorkersByMonth(startOfMonth, endOfMonth);
        Long existingWorkers = workerRepository.countExistingWorkersByMonth(endOfMonth);
        Long terminatedWorkers = workerRepository.countTerminatedWorkersByMonth(startOfMonth, endOfMonth);

        return new WorkerReportDto(newWorkers, existingWorkers, terminatedWorkers);
    }

    public WorkerReportDto getYearlyWorkerReport(int year) {
        Long newWorkers = workerRepository.countNewWorkersByYear(year);
        Long existingWorkers = workerRepository.countExistingWorkersByYear(year);
        Long terminatedWorkers = workerRepository.countTerminatedWorkersByYear(year);

        return new WorkerReportDto(newWorkers, existingWorkers, terminatedWorkers);
    }
}