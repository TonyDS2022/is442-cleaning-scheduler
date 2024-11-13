package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

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
@RequestMapping("/api/v0.1/leave-applications")
public class    LeaveApplicationController {

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @PostMapping("/{workerId}/apply-annual-leave")
    public ResponseEntity<String> applyAnnualLeave(@PathVariable("workerId") Long workerId,
                                                   @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                   @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            leaveApplicationService.applyAnnualLeave(workerId, startDate, endDate);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Annual leave applied successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{workerId}/apply-medical-leave")
    public ResponseEntity<String> applyMedicalLeave(@PathVariable("workerId") Long workerId,
                                                    @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            leaveApplicationService.applyMedicalLeave(workerId, startDate, endDate);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Medical leave applied successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
