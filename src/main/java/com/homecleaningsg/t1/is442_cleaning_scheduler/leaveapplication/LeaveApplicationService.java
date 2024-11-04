package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.homecleaningsg.t1.is442_cleaning_scheduler.imagehandler.GoogleImageService;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
// @RequiredArgsConstructor // kheng idk what this does
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final GoogleImageService googleImageService;
    private final LeavePolicyService leavePolicyService;

    @Autowired
    public LeaveApplicationService(
            LeaveApplicationRepository leaveApplicationRepository,
            GoogleImageService googleImageService,
            LeavePolicyService leavePolicyService
    ) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.googleImageService = googleImageService;
        this.leavePolicyService = leavePolicyService;
    }

    // Method to create a new LeaveApplication directly from the object
    public LeaveApplication createLeaveApplication(LeaveApplication leaveApplication, MultipartFile file) throws Exception {

        // Check for duplicate image upload using hash
        if (file != null && !file.isEmpty()) {
            String imageHash = computeImageHash(file);
            if (leaveApplicationRepository.existsByImageHash(imageHash)) {
                throw new IllegalArgumentException("Duplicate image upload detected");
            }
            leaveApplication.setImageHash(imageHash);
            // Rename and upload file
            String newFileName = googleImageService.uploadImage(file);
            leaveApplication.setFileName(newFileName);
        }
        if (leaveApplication.getLeaveType() == LeaveType.MEDICAL) {
            int availableMedicalLeave = leavePolicyService.getMedicalLeaveBalance(leaveApplication.getWorkerId());
            if (availableMedicalLeave < 1) {
                throw new IllegalArgumentException("No medical leave balance remaining");
            }
            // Deduct leave balance for new application
            leaveApplication.setMedicalLeaveBalance(availableMedicalLeave - 1);
        } else if (leaveApplication.getLeaveType() == LeaveType.OTHERS) {
            int availableOtherLeave = leavePolicyService.getOtherLeaveBalance(leaveApplication.getWorkerId());
            if (availableOtherLeave < 1) {
                throw new IllegalArgumentException("No medical leave balance remaining");
            }
            // Deduct leave balance for new application
            leaveApplication.setOtherLeaveBalance(availableOtherLeave - 1);
        }

        return leaveApplicationRepository.save(leaveApplication);
    }

    // Get all pending applications for a worker
    public List<LeaveApplication> getPendingApplicationsByWorkerId(Long workerId) {
        return leaveApplicationRepository.findByWorkerIdAndApplicationStatus(
                workerId,
                LeaveApplication.ApplicationStatus.PENDING);
    }

    // Get all non-pending (historical) applications for a worker
    public List<LeaveApplication> getHistoricalApplicationsByWorkerId(Long workerId) {
        return leaveApplicationRepository.findByWorkerIdAndApplicationStatusNot(
                workerId,
                LeaveApplication.ApplicationStatus.PENDING);
    }

    // Get the most recent approved application for a worker
    public Optional<LeaveApplication> getMostRecentApprovedApplication(Long workerId) {
        return leaveApplicationRepository.findTopByWorkerIdAndApplicationStatusOrderByLeaveSubmittedDateDescLeaveSubmittedTimeDesc(
                workerId,
                LeaveApplication.ApplicationStatus.APPROVED);
    }

    // Get all pending applications for admin to view
    public List<LeaveApplication> getPendingApplicationsByAdminId(Long adminId) {
        return leaveApplicationRepository.findByAdminIdAndApplicationStatusOrderByWorkerId(
                adminId,
                LeaveApplication.ApplicationStatus.PENDING);
    }

    // Post edited changes to repository
    public LeaveApplication vetLeaveApplication(
            Long adminId,
            Long applicationId,
            LeaveApplication.ApplicationStatus newStatus
    ) {
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid leave application ID"));

        // Check if the admin is authorized to vet this application
        if (!leaveApplication.getAdminId().equals(adminId)) {
            throw new IllegalArgumentException("Admin is not authorized to vet this leave application");
        }

        leaveApplication.setApplicationStatus(newStatus);

        // Update leave balances if approved
        if (newStatus == LeaveApplication.ApplicationStatus.REJECTED) {
            if (leaveApplication.getLeaveType() == LeaveType.MEDICAL) {
                leaveApplication.setMedicalLeaveBalance(leaveApplication.getMedicalLeaveBalance() + 1);
            } else if (leaveApplication.getLeaveType() == LeaveType.OTHERS) {
                int updatedBalance = leavePolicyService.getOtherLeaveBalance(leaveApplication.getWorkerId()) - 1;
                leaveApplication.setMedicalLeaveBalance(leaveApplication.getMedicalLeaveBalance() + 1);
            }
        }

        return leaveApplicationRepository.save(leaveApplication);
    }

    public List<LeaveApplication> retrieveAbsentEmployee(Long adminId, String leavePeriod) {
        // Do take note that list returned can be empty
        LocalDate currentDate = LocalDate.now();
        if (leavePeriod.equalsIgnoreCase("Upcoming")) {
            // Retrieve all leave applications for the given admin ID where leaveStart is in the future
            return leaveApplicationRepository.findByAdminIdAndLeaveStartDateAfterAndApplicationStatus(
                    adminId,
                    currentDate,
                    LeaveApplication.ApplicationStatus.APPROVED
            );
        } else if (leavePeriod.equalsIgnoreCase("Current")) {
            // Retrieve all leave applications for the given admin ID where leaveStart is before or equal to the current date and leaveEnd is after or equal to the current date
            return leaveApplicationRepository.findByAdminIdAndLeaveStartDateBeforeAndLeaveEndDateAfterAndApplicationStatus(
                    adminId,
                    currentDate,
                    currentDate,
                    LeaveApplication.ApplicationStatus.APPROVED
            );
        } else if (leavePeriod.equalsIgnoreCase("Past")) {
            // Retrieve all leave applications for the given admin ID where leaveEnd is in the past
            return leaveApplicationRepository.findByAdminIdAndLeaveEndDateBeforeAndApplicationStatus(
                    adminId,
                    currentDate,
                    LeaveApplication.ApplicationStatus.APPROVED
            );
        } else {
            throw new IllegalArgumentException("Invalid leave period specified");
        }
    }

    public List<LeaveApplication> retrieveEmployeeLeaveBalances(Long adminId) {
        return leaveApplicationRepository.findLatestApprovedLeaveBalanceByAdminId(adminId);
    }

    String computeImageHash(MultipartFile file) throws IOException {
        return DigestUtils.sha256Hex(file.getInputStream());
    }

    // // Method to create a new LeaveApplication directly from the object
    // public LeaveApplication createLeaveApplication(LeaveApplication leaveApplication) {
    //
    //     // Save the leave application to the repository
    //     return leaveApplicationRepository.save(leaveApplication);
    // }
    //
    // // Get all pending applications for a worker
    // public List<LeaveApplication> getPendingApplicationsByWorkerId(Long workerId) {
    //     return leaveApplicationRepository.findByWorkerIdAndApplicationStatus(
    //             workerId,
    //             LeaveApplication.ApplicationStatus.PENDING
    //     );
    // }
    //
    // // Get all non-pending (historical) applications for a worker
    // public List<LeaveApplication> getHistoricalApplicationsByWorkerId(Long workerId) {
    //     return leaveApplicationRepository.findByWorkerIdAndApplicationStatusNot(
    //             workerId,
    //             LeaveApplication.ApplicationStatus.PENDING
    //     );
    // }
    //
    // // Get the most recent approved application for a worker
    // public Optional<LeaveApplication> getMostRecentApprovedApplication(Long workerId) {
    //     return leaveApplicationRepository.findTopByWorkerIdAndApplicationStatusOrderByLeaveSubmittedDateDescLeaveSubmittedTimeDesc(
    //             workerId,
    //             LeaveApplication.ApplicationStatus.APPROVED
    //     );
    // }

    public boolean isWorkerOnLeave(Long workerId, LocalDate shiftStartDate, LocalTime shiftStartTime, LocalDate shiftEndDate, LocalTime shiftEndTime) {
        List<LeaveApplication> historicalApplications = getHistoricalApplicationsByWorkerId(workerId);

        return historicalApplications.stream().anyMatch(application -> {

            LocalDate leaveStartDate = application.getLeaveStartDate();
            LocalTime leaveStartTime = application.getLeaveStartTime();
            LocalDate leaveEndDate = application.getLeaveEndDate();
            LocalTime leaveEndTime = application.getLeaveEndTime();

            return (shiftStartDate.isEqual(leaveStartDate) && !shiftStartTime.isBefore(leaveStartTime)) || // Same start day and shift start after or at leave start
                    (shiftEndDate.isEqual(leaveEndDate) && !shiftEndTime.isAfter(leaveEndTime)) ||          // Same end day and shift end before or at leave end
                    (shiftStartDate.isAfter(leaveStartDate) && shiftEndDate.isBefore(leaveEndDate));
        });
    }
}
