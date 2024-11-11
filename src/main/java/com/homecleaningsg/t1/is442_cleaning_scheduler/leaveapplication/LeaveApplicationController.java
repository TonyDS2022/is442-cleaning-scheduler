package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0.1/leave-applications")
public class    LeaveApplicationController {

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private LeavePolicyService leavePolicyService;

    // Endpoint to create a new leave application
    @PostMapping("/{workerId}/application/submission")
    public ResponseEntity<?> createLeaveApplication(@PathVariable Long workerId,
                                                    @RequestParam Long adminId,
                                                    @RequestParam LeaveType leaveType,
                                                    @RequestParam LocalDate leaveStartDate,
                                                    @RequestParam LocalTime leaveStartTime,
                                                    @RequestParam LocalDate leaveEndDate,
                                                    @RequestParam LocalTime leaveEndTime,
                                                    @RequestParam(required = false) MultipartFile file) {
        try {
            // Check leave balance using LeavePolicyService
            int medicalLeaveBalance = leavePolicyService.getMedicalLeaveBalance(workerId);
            int otherLeaveBalance = leavePolicyService.getOtherLeaveBalance(workerId);

            if (leaveType == LeaveType.MEDICAL && medicalLeaveBalance < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No medical leave balance remaining");
            }
            if (leaveType == LeaveType.OTHERS && otherLeaveBalance < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No other leave balance remaining");
            }

            LeaveApplication leaveApplication = new LeaveApplication(
                    workerId,
                    adminId,
                    leaveType,
                    file != null ? file.getOriginalFilename() : null,
                    file != null ? leaveApplicationService.computeImageHash(file) : null,
                    leaveStartDate,
                    leaveStartTime,
                    leaveEndDate,
                    leaveEndTime,
                    LocalDate.now(),
                    LocalTime.now(),
                    LeaveApplication.ApplicationStatus.PENDING,
                    medicalLeaveBalance,
                    otherLeaveBalance
            );

            LeaveApplication createdLeaveApplication = leaveApplicationService.createLeaveApplication(leaveApplication, file);

            return ResponseEntity.ok(createdLeaveApplication);


        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate Image Submitted: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the image file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the leave application: " + e.getMessage());
        }
    }

    @GetMapping("/{workerId}/application")
    public ResponseEntity<Map<String, Object>> getPendingAndMostRecentApprovedApplication(@PathVariable Long workerId) {
        // Retrieve pending applications
        List<LeaveApplication> pendingApplications = leaveApplicationService.getPendingApplicationsByWorkerId(workerId);

        // Retrieve the most recent approved application (if any)
        Optional<LeaveApplication> mostRecentApprovedApplication = leaveApplicationService.getMostRecentApprovedApplication(workerId);


        int medical_leave_balance = leavePolicyService.getMedicalLeaveBalance(workerId);
        int other_leave_balance = leavePolicyService.getOtherLeaveBalance(workerId);


        // Create a response map to return both pending applications and the most recent approved application
        Map<String, Object> response = new HashMap<>();
        response.put("pendingApplications", pendingApplications);
        response.put("mostRecentApprovedApplication", mostRecentApprovedApplication);
        response.put("medical_leave_balance", medical_leave_balance);
        response.put("other_leave_balance", other_leave_balance);

        return ResponseEntity.ok(response);
    }

    // Endpoint to get all historical (non-pending) applications for a specific worker
    @GetMapping("/{workerId}/application/history")
    public ResponseEntity<List<LeaveApplication>> getHistoricalApplicationsByWorkerId(@PathVariable Long workerId) {
        List<LeaveApplication> applications = leaveApplicationService.getHistoricalApplicationsByWorkerId(workerId);
        return ResponseEntity.ok(applications);
    }

    // Endpoint to get all pending applications for admin
    @GetMapping("/admin/{adminId}/view_applications")
    public ResponseEntity<List<LeaveApplication>> getPendingApplicationsByAdminId(@PathVariable Long adminId) {
        List<LeaveApplication> pendingApplications = leaveApplicationService.getPendingApplicationsByAdminId(adminId);
        return ResponseEntity.ok(pendingApplications);
    }

    // Endpoint to change the application status from admin
    @PostMapping("/admin/{adminId}/vet_applications")
    public ResponseEntity<?> vetLeaveApplication(@PathVariable Long adminId,
                                                 @RequestParam Long applicationId,
                                                 @RequestParam LeaveApplication.ApplicationStatus applicationStatus) {
        try {
            LeaveApplication updatedApplication = leaveApplicationService.vetLeaveApplication(adminId, applicationId, applicationStatus);
            return ResponseEntity.ok(updatedApplication);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid application or action: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the leave application: " + e.getMessage());
        }
    }

    // Endpoint for admin to set dynamic leave balances
    @PostMapping("/admin/set_leave_balances")
    public ResponseEntity<?> setLeaveBalances(@RequestParam int medicalLeaveBalance,
                                              @RequestParam int otherLeaveBalance) {
        try {
            leavePolicyService.setDynamicMedicalLeaveBalance(medicalLeaveBalance);
            leavePolicyService.setDynamicOtherLeaveBalance(otherLeaveBalance);
            return ResponseEntity.ok("Leave balances updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating leave balances: " + e.getMessage());
        }
    }

    // Endpoint for admin to view all managed employees that are on leave
    @GetMapping("/admin/{adminId}/view_absent_employees")
    // Do take note that list returned can be empty
    // leavePeriod expects "Upcoming", "Current", "Past"
    public ResponseEntity<List<LeaveApplication>> getAbsentEmployees(@PathVariable Long adminId,
                                                                     @RequestParam String leavePeriod) {
        List<LeaveApplication> employeeList = leaveApplicationService.retrieveAbsentEmployee(adminId, leavePeriod);
        return ResponseEntity.ok(employeeList);
    }

    // Endpoint for admin to view all manged employee's leave balances
    @GetMapping("/admin/{adminId}/view_employees_leave")
    public ResponseEntity<List<LeaveApplication>> retrieveEmployeeLeaveBalances(@PathVariable Long adminId) {
        List<LeaveApplication> employeeLeaveBalances = leaveApplicationService.retrieveEmployeeLeaveBalances(adminId);
        return ResponseEntity.ok(employeeLeaveBalances);
    }
}
