package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Retrieve all workers",
            description = "Fetches a list of all workers registered in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the list of workers.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Worker.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to retrieve workers due to an invalid request or server issue.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Invalid argument passed.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getAllWorkers() {
        try {
            List<Worker> workers = workerService.getAllWorkers();
            return ResponseEntity.ok(workers);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{username}")
    @Operation(
            summary = "Retrieve a worker by username",
            description = "Fetches the details of a specific worker using their username.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the worker details.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Worker.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to find the worker due to invalid or missing username.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Worker with username 'john_doe' not found.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getWorkerByUsername(@PathVariable("username") String username) {
        try {
            Worker worker = workerService.getWorkerByUsername(username);
            return ResponseEntity.ok(worker);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{workerId}/getResidentialAddressOfWorker")
    @Operation(
            summary = "Retrieve the residential address of a worker",
            description = "Fetches the residential address of a specific worker using their worker ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the residential address of the worker.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Location.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to retrieve the address due to invalid worker ID or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Worker with ID 123 not found.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getResidentialAddressOfWorker(@PathVariable Long workerId) {
        try {
            Location location = workerService.getResidentialAddressOfWorker(workerId);
            return ResponseEntity.ok(location);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/{workerId}/addResidentialAddressToWorker/")
    @Operation(
            summary = "Add a residential address to a worker",
            description = "Adds a residential address to a specific worker using their worker ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully added the residential address to the worker.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Location added to worker successfully.")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - Unable to add the address due to invalid worker ID or location.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Worker with ID 123 not found.")
                            )
                    )
            }
    )
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
    @Operation(
            summary = "Add a worker",
            description = "Adds a new worker to the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully added the worker.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(implementation = Worker.class, exampleClasses = Worker.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to add the worker due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Unable to add worker.")
                            )
                    )
            }
    )
    public ResponseEntity<String> addWorker(@RequestBody Worker worker) {
        try {
            workerService.addWorker(worker);
            return ResponseEntity.status(HttpStatus.OK).body("Worker added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add contract.");
        }
    }

    @PutMapping("/update-worker/{workerId}")
    @Operation(
            summary = "Update worker details",
            description = "Updates the details of a specific worker using their worker ID.",
            parameters = {
                    @Parameter(name = "workerId", description = "The ID of the worker to be updated", required = true, example = "1"),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The updated worker object containing new details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Worker.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Worker details updated successfully.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Worker details updated successfully.")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to update worker details due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Unable to update worker details.")
                            )
                    )
            }
    )
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
    @Operation(
            summary = "Deactivate a worker",
            description = "Deactivates a specific worker by their worker ID and removes them from all associated future shifts.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Worker successfully deactivated and removed from future shifts.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "The worker has been successfully deactivated, and removed from all associated future shifts.")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to deactivate the worker due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Unable to deactivate worker.")
                            )
                    )
            }
    )
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