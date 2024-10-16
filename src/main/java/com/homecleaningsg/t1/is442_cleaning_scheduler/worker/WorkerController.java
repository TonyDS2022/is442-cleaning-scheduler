package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v0.1/workers")
public class WorkerController {
    private final WorkerService workerService;

    @Autowired
    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping
    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }


    @GetMapping("/{username}")
    public Worker getWorkerByUsername(@PathVariable("username") String username) {
        return workerService.getWorkerByUsername(username);
    }

    @GetMapping("/{workerId}/getResidentialAddressOfWorker")
    public Location getResidentialAddressOfWorker(@PathVariable Long workerId) {
        return workerService.getResidentialAddressOfWorker(workerId);
    }

    @PostMapping("/{workerId}/addResidentialAddressToWorker/{locationId}")
    public ResponseEntity<String> addResidentialAddressToWorker(
            @PathVariable Long workerId,
            @PathVariable Long locationId) {
        try {
            workerService.addResidentialAddressToWorker(workerId, locationId);
            return ResponseEntity.ok("Location added to worker successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Return 404 if worker or location is not found
        }
    }
}
