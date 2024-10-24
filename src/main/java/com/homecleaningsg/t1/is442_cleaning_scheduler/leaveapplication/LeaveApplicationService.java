package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.homecleaningsg.t1.is442_cleaning_scheduler.imagehandler.GoogleImageService;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final GoogleImageService googleImageService;

    public LeaveApplication createLeaveApplication(LeaveApplication leaveApplication, MultipartFile file) throws Exception {

        // Check if leave is medical and file is required
        // if (leaveApplication.getLeaveType() == LeaveType.MEDICAL && (file == null || file.isEmpty())) {
        //    throw new IllegalArgumentException("Medical leave requires a file upload");
        // }

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

        // TO HAVE THE FUNCTION TO REJECT CREATION OF APPLICATION WHEN LEAVE IS ALL USED UP

        return leaveApplicationRepository.save(leaveApplication);
    }

    // Get all pending applications for a worker
    public List<LeaveApplication> getPendingApplicationsByWorkerId(Long workerId) {
        return leaveApplicationRepository.findByWorkerIdAndApplicationStatus(workerId, ApplicationStatus.PENDING);
    }

    // Get all non-pending (historical) applications for a worker
    public List<LeaveApplication> getHistoricalApplicationsByWorkerId(Long workerId) {
        return leaveApplicationRepository.findByWorkerIdAndApplicationStatusNot(workerId, ApplicationStatus.PENDING);
    }

    // Get the most recent approved application for a worker
    public Optional<LeaveApplication> getMostRecentApprovedApplication(Long workerId) {
        return leaveApplicationRepository.findTopByWorkerIdAndApplicationStatusOrderByApplicationSubmittedDesc(workerId, ApplicationStatus.APPROVED);
    }

    //
    public void updateLeaveBalance(LeaveApplication leaveApplication) {
        if (leaveApplication.getApplicationStatus() == ApplicationStatus.APPROVED) {
            if (leaveApplication.getLeaveType() == LeaveType.MEDICAL) {
                leaveApplication.setMedicalLeaveBalance(leaveApplication.getMedicalLeaveBalance() - 1);
            } else if (leaveApplication.getLeaveType() == LeaveType.OTHERS) {
                leaveApplication.setOtherLeaveBalance(leaveApplication.getOtherLeaveBalance() - 1);
            }
        }
    }

    private String computeImageHash(MultipartFile file) throws IOException {
        return DigestUtils.sha256Hex(file.getInputStream());
    }
}
