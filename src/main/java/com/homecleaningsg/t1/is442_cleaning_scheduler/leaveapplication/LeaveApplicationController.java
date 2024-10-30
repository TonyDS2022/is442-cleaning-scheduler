package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v0.1/leave-applications")
public class    LeaveApplicationController {

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    // Endpoint to create a new leave application
    @PostMapping("/worker/{workerId}/application-submission")
    public ResponseEntity<LeaveApplication> createLeaveApplication(@RequestBody LeaveApplication leaveApplication) {
        LeaveApplication savedApplication = leaveApplicationService.createLeaveApplication(leaveApplication);
        return ResponseEntity.ok(savedApplication);
    }

    // Endpoint to get all pending applications and the most recent approved application for a worker
    @GetMapping("/worker/{workerId}/pending-with-approved")
    public ResponseEntity<Map<String, Object>> getPendingAndMostRecentApprovedApplication(@PathVariable Long workerId) {
        // Retrieve pending applications
        List<LeaveApplication> pendingApplications = leaveApplicationService.getPendingApplicationsByWorkerId(workerId);

        // Retrieve the most recent approved application (if any)
        Optional<LeaveApplication> mostRecentApprovedApplication = leaveApplicationService.getMostRecentApprovedApplication(workerId);

        // Create a response map to return both pending applications and the most recent approved application
        Map<String, Object> response = new HashMap<>();
        response.put("pendingApplications", pendingApplications);
        response.put("mostRecentApprovedApplication", mostRecentApprovedApplication.orElse(null));

        return ResponseEntity.ok(response);
    }

    // Endpoint to get all historical (non-pending) applications for a specific worker
    @GetMapping("/worker/{workerId}/history")
    public ResponseEntity<List<LeaveApplication>> getHistoricalApplicationsByWorkerId(@PathVariable Long workerId) {
        List<LeaveApplication> applications = leaveApplicationService.getHistoricalApplicationsByWorkerId(workerId);
        return ResponseEntity.ok(applications);
    }
}
