package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveApplicationService {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    // Method to save new leave application
    public LeaveApplication saveLeaveApplication(LeaveApplication leaveApplication) {
        return leaveApplicationRepository.save(leaveApplication);
    }

    // Method to find an application by ID
    public Optional<LeaveApplication> findById(Long applicationId) {
        return leaveApplicationRepository.findById(applicationId);
    }

    // Method to get all applications
    public List<LeaveApplication> findAllApplications() {
        return leaveApplicationRepository.findAll();
    }

    // Additional methods for business logic, e.g., updating status
    public LeaveApplication updateLeaveApplication(LeaveApplication leaveApplication) {
        return leaveApplicationRepository.save(leaveApplication);
    }

    public void deleteLeaveApplication(Long applicationId) {
        leaveApplicationRepository.deleteById(applicationId);
    }
}
