package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final LocationRepository locationRepository;
    private final ShiftService shiftService;

    @Autowired
    public WorkerService(WorkerRepository workerRepository, LocationRepository locationRepository, ShiftService shiftService) {
        this.workerRepository = workerRepository;
        this.locationRepository = locationRepository;
        this.shiftService = shiftService;
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker getWorkerByUsername(String username) {
        return workerRepository.findByUsername(username).orElse(null);
    }

    public void addResidentialAddressToWorker(Long workerId, Long locationId) {
        // Find the worker by ID
        Optional<Worker> workerOptional = workerRepository.findById(workerId);
        // Find the location by ID
        Optional<Location> locationOptional = locationRepository.findById(locationId);

        if (workerOptional.isPresent() && locationOptional.isPresent()) {
            Worker worker = workerOptional.get();
            Location location = locationOptional.get();
            // Set the location to the worker
            worker.setLocation(location);
            // Save the updated worker
            workerRepository.save(worker);
        } else {
            // Handle cases where the worker or location is not found
            throw new RuntimeException("Worker or Location not found");
        }
    }

    public Location getResidentialAddressOfWorker(Long workerId) {
        return workerRepository.findById(workerId)
                .map(Worker::getLocation)
                .orElse(null); // Return null if worker not found
    }
}
