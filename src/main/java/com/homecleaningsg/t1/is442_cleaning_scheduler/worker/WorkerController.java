package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
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


    @PostMapping("/add-worker/")
    public ResponseEntity<String> createWorker(@RequestBody Worker worker) {
        try {
            return workerService.addWorker(worker);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Unable to add worker");
        }
    }

    @PutMapping("/update-worker/{workerId}")
    public ResponseEntity<String> updateWorker(
            @PathVariable("workerId") Long workerId, @RequestBody Worker updatedWorker) {
        try {
            return workerService.updateWorker(workerId, updatedWorker);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Unable to update worker details.");
        }
    }

    // localhost:8080/api/v0.1/workers/deactivate-worker/1
    @PutMapping("/deactivate-worker/{workerId}") // Endpoint to deactivate a worker
    public ResponseEntity<String> deactivateWorker(@PathVariable("workerId") Long workerId) {
        try {
            return workerService.deactivateWorker(workerId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Unable to deactivate worker.");
        }
    }
}
