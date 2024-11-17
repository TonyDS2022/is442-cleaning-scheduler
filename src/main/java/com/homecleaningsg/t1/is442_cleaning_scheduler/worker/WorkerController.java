package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
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
    public ResponseEntity<?> getAllWorkers() {
        try {
            List<Worker> workers = workerService.getAllWorkers();
            return ResponseEntity.ok(workers);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getWorkerByUsername(@PathVariable("username") String username) {
        try {
            Worker worker = workerService.getWorkerByUsername(username);
            return ResponseEntity.ok(worker);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{workerId}/getResidentialAddressOfWorker")
    public ResponseEntity<?> getResidentialAddressOfWorker(@PathVariable Long workerId) {
        try {
            Location location = workerService.getResidentialAddressOfWorker(workerId);
            return ResponseEntity.ok(location);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/{workerId}/addResidentialAddressToWorker/")
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
            return ResponseEntity.status(HttpStatus.OK).body("Worker added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add contract.");
        }
    }

    @PutMapping("/update-worker/{workerId}")
    public ResponseEntity<String> updateWorker(
            @PathVariable("workerId") Long workerId, @RequestBody Worker updatedWorker) {
        try {
            workerService.updateWorker(workerId, updatedWorker);
            return ResponseEntity.status(HttpStatus.OK).body("Worker details updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update worker details.");
        }
    }

    // localhost:8080/api/v0.1/workers/deactivate-worker/2
    @PutMapping("/deactivate-worker/{workerId}")
    public ResponseEntity<String> deactivateWorker(@PathVariable("workerId") Long workerId) {
        try {
            workerService.deactivateWorker(workerId);
            return ResponseEntity.status(HttpStatus.OK).body("The worker has been successfully deactivated, and removed from all associated future shifts.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to deactivate worker.");
        }
    }

    // localhost:8080/api/v0.1/workers/leave-balance/
    @GetMapping("/leave-balance/")
    public ResponseEntity<?> getWorkerLeaveBalance(@RequestParam Long workerId,
                                      @RequestParam LeaveApplication.LeaveType leaveType,
                                      @RequestParam int year) {
        try{
            long leaveBalance = workerService.getWorkerLeaveBalance(workerId, leaveType, year);
            return ResponseEntity.ok(leaveBalance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}