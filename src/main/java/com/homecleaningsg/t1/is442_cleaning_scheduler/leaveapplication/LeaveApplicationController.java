package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v0.1/leave-applications")
public class LeaveApplicationController {

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    // Endpoint to create a new leave application
    @PostMapping("/worker/{workerId}/application-submission")
    public ResponseEntity<?> createLeaveApplication(@RequestParam LeaveType leaveType,
                                                    @RequestParam OffsetDateTime affectedShiftStart,
                                                    @RequestParam OffsetDateTime affectedShiftEnd,
                                                    @RequestParam MultipartFile file) {
        try {
            Long workerId = getLoggedInWorkerId();
            Long adminId = getAdminForWorker(workerId);

            LeaveApplication leaveApplication = new LeaveApplication();
            leaveApplication.setWorkerId(workerId);
            leaveApplication.setAdminId(adminId);
            leaveApplication.setLeaveType(leaveType);
            leaveApplication.setAffectedShiftStart(affectedShiftStart);
            leaveApplication.setAffectedShiftEnd(affectedShiftEnd);
            leaveApplication.setApplicationSubmitted(OffsetDateTime.now());
            leaveApplication.setApplicationStatus(ApplicationStatus.PENDING);

            LeaveApplication createdLeaveApplication = leaveApplicationService.createLeaveApplication(leaveApplication, file);
            leaveApplicationService.updateLeaveBalance(createdLeaveApplication);

            return ResponseEntity.ok(createdLeaveApplication);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate Image Submitted", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while creating the leave application", e);
        }
    }

    @GetMapping("/worker/{workerId}/pending-with-approved")
    public ResponseEntity<Map<String, Object>> getPendingAndMostRecentApprovedApplication(@PathVariable Long workerId) {
        // Retrieve pending applications
        List<LeaveApplication> pendingApplications = leaveApplicationService.getPendingApplicationsByWorkerId(workerId);

        // Retrieve the most recent approved application (if any)
        Optional<LeaveApplication> mostRecentApprovedApplication = leaveApplicationService.getMostRecentApprovedApplication(workerId);

        // Create a response map to return both pending applications and the most recent approved application
        Map<String, Object> response = new HashMap<>();
        response.put("pendingApplications", pendingApplications);
        response.put("mostRecentApprovedApplication", mostRecentApprovedApplication);

        return ResponseEntity.ok(response);
    }

    // Endpoint to get all historical (non-pending) applications for a specific worker
    @GetMapping("/worker/{workerId}/history")
    public ResponseEntity<List<LeaveApplication>> getHistoricalApplicationsByWorkerId(@PathVariable Long workerId) {
        List<LeaveApplication> applications = leaveApplicationService.getHistoricalApplicationsByWorkerId(workerId);
        return ResponseEntity.ok(applications);
    }

    private Long getLoggedInWorkerId() {
        // Mock function to retrieve logged-in worker ID
        return 1L;
    }

    private Long getAdminForWorker(Long workerId) {
        // Mock function to retrieve admin ID for worker
        return 2L;
    }
}
