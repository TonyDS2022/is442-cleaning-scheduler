package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.homecleaningsg.t1.is442_cleaning_scheduler.imagehandler.GoogleImageService;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final GoogleImageService googleImageService;

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

    String computeImageHash(MultipartFile file) throws IOException {
        return DigestUtils.sha256Hex(file.getInputStream());
    }


}
