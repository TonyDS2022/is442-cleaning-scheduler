package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequiredArgsConstructor
@RequestMapping("/api/v0.1/leave-applications")
public class    LeaveApplicationController {

    private final LeaveApplicationService leaveApplicationService;

    // localhost:8080/api/v0.1/leave-applications/1/apply-annual-leave/
    @PostMapping("/{workerId}/apply-annual-leave/")
    public ResponseEntity<String> applyAnnualLeave(@PathVariable("workerId") Long workerId,
                                                   @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                   @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            leaveApplicationService.applyAnnualLeave(workerId, startDate, endDate);
            return ResponseEntity.status(HttpStatus.OK).body("Annual leave applied successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // localhost:8080/api/v0.1/leave-applications/4/apply-medical-leave/
    @PostMapping("/{workerId}/apply-medical-leave/")
    public ResponseEntity<String> applyMedicalLeave(@PathVariable("workerId") Long workerId,
                                                    @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            leaveApplicationService.applyMedicalLeave(workerId, startDate, endDate);
            return ResponseEntity.status(HttpStatus.OK).body("Medical leave applied successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{adminId}/get-pending-leave-applications/")
    public List<LeaveApplicationAdminViewDto> getPendingLeaveApplications(@PathVariable("adminId") Long adminId) {
        return leaveApplicationService.getPendingLeaveApplicationsForAdmin(adminId);
    }
}
