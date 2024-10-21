package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    public LeaveApplicationService(LeaveApplicationRepository leaveApplicationRepository) {
        this.leaveApplicationRepository = leaveApplicationRepository;
    }

    // Method to create a new LeaveApplication directly from the object
    public LeaveApplication createLeaveApplication(LeaveApplication leaveApplication) {

        // Business logic: If leave type is medical, a file must be attached
        if (leaveApplication.getLeaveType() == LeaveType.MEDICAL &&
                (leaveApplication.getFileName() == null || leaveApplication.getFileName().isEmpty())) {
            throw new IllegalArgumentException("A file must be attached for medical leave.");
        }

        // Save the leave application to the repository
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
        return leaveApplicationRepository.findLastApprovedApplication(workerId, ApplicationStatus.APPROVED);
    }

    public void deleteLeaveApplication(Long applicationId) {
        leaveApplicationRepository.deleteById(applicationId);
    }
}
