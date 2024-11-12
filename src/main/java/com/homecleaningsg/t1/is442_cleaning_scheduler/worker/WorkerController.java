package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/{workerId}/addResidentialAddressToWorker")
    public ResponseEntity<String> addResidentialAddressToWorker(
            @PathVariable Long workerId,
            @RequestParam String streetAddress,
            @RequestParam String postalCode,
            @RequestParam String unitNumber
            ) {
        try {
            workerService.addResidentialAddressToWorker(workerId, streetAddress, postalCode, unitNumber);
            return ResponseEntity.ok("Location added to worker successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Return 404 if worker or location is not found
        }
    }

    @PostMapping("/add-worker/")
    public ResponseEntity<String> addWorker(@RequestBody Worker worker) {
        try {
            workerService.addWorker(worker);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Worker added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add contract.");
        }
    }

    @PutMapping("/update-worker/{workerId}")
    public ResponseEntity<String> updateWorker(
            @PathVariable("workerId") Long workerId, @RequestBody Worker updatedWorker) {
        try {
            workerService.updateWorker(workerId, updatedWorker);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Worker details updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update worker details.");
        }
    }

    // localhost:8080/api/v0.1/workers/deactivate-worker/2
    @PutMapping("/deactivate-worker/{workerId}")
    public ResponseEntity<String> deactivateWorker(@PathVariable("workerId") Long workerId) {
        try {
            workerService.deactivateWorker(workerId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("The worker has been successfully deactivated, and removed from all associated future shifts.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to deactivate worker.");
        }
    }
}