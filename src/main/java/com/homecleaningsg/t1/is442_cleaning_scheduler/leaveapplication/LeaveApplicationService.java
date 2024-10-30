package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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
        return leaveApplicationRepository.findTopByWorkerIdAndApplicationStatusOrderByApplicationSubmittedDesc(workerId, ApplicationStatus.APPROVED);
    }

    public boolean isWorkerOnLeave(Long workerId, LocalDate shiftStartDate, LocalTime shiftStartTime, LocalDate shiftEndDate, LocalTime shiftEndTime) {
        List<LeaveApplication> historicalApplications = getHistoricalApplicationsByWorkerId(workerId);

        return historicalApplications.stream().anyMatch(application -> {
            OffsetDateTime leaveStart = application.getAffectedShiftStart();
            OffsetDateTime leaveEnd = application.getAffectedShiftEnd();

            LocalDate leaveStartDate = leaveStart.toLocalDate();
            LocalTime leaveStartTime = leaveStart.toLocalTime();
            LocalDate leaveEndDate = leaveEnd.toLocalDate();
            LocalTime leaveEndTime = leaveEnd.toLocalTime();

            return (shiftStartDate.isEqual(leaveStartDate) && !shiftStartTime.isBefore(leaveStartTime)) || // Same start day and shift start after or at leave start
                    (shiftEndDate.isEqual(leaveEndDate) && !shiftEndTime.isAfter(leaveEndTime)) ||          // Same end day and shift end before or at leave end
                    (shiftStartDate.isAfter(leaveStartDate) && shiftEndDate.isBefore(leaveEndDate));
        });
    }

}
