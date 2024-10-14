package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0.1/leave-applications")
public class LeaveApplicationController {

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    // Endpoint to create a new leave application
    @PostMapping
    public ResponseEntity<LeaveApplication> createLeaveApplication(@RequestBody LeaveApplication leaveApplication) {
        LeaveApplication savedApplication = leaveApplicationService.saveLeaveApplication(leaveApplication);
        return ResponseEntity.ok(savedApplication);
    }

    // Endpoint to get a leave application by ID
    @GetMapping("/{id}")
    public ResponseEntity<LeaveApplication> getLeaveApplicationById(@PathVariable Long id) {
        return leaveApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to retrieve all leave applications
    @GetMapping
    public ResponseEntity<List<LeaveApplication>> getAllLeaveApplications() {
        List<LeaveApplication> applications = leaveApplicationService.findAllApplications();
        return ResponseEntity.ok(applications);
    }

    // Endpoint to update an existing leave application
    @PutMapping("/{id}")
    public ResponseEntity<LeaveApplication> updateLeaveApplication(@PathVariable Long id, @RequestBody LeaveApplication leaveApplication) {
        // You may want to check if the application exists before updating
        return leaveApplicationService.findById(id)
                .map(existingApplication -> {
                    leaveApplication.setApplicationId(id); // Ensure the ID remains the same
                    LeaveApplication updatedApplication = leaveApplicationService.updateLeaveApplication(leaveApplication);
                    return ResponseEntity.ok(updatedApplication);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to delete a leave application
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveApplication(@PathVariable Long id) {
        leaveApplicationService.deleteLeaveApplication(id);
        return ResponseEntity.noContent().build();
    }
}
