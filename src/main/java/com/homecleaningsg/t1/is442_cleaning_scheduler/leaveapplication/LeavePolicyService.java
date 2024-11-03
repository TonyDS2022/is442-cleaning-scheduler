package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class LeavePolicyService {

    private final LeaveApplicationRepository leaveApplicationRepository;

    @Getter
    @Setter
    private int dynamicMedicalLeaveBalance = 5;
    @Getter
    @Setter
    private int dynamicOtherLeaveBalance = 10;

    public int getMedicalLeaveBalance(Long workerId) {
        // Fetch the most recent approved leave balance for medical leave
        Optional<LeaveApplication> mostRecentApprovedMedicalLeave = leaveApplicationRepository
                .findTopByWorkerIdAndLeaveTypeAndApplicationStatusOrderByLeaveSubmittedDateDescLeaveSubmittedTimeDesc(
                        workerId,
                        LeaveType.MEDICAL,
                        LeaveApplication.ApplicationStatus.APPROVED);

        int approvedBalance = mostRecentApprovedMedicalLeave
                .map(LeaveApplication::getMedicalLeaveBalance)
                .orElse(getDynamicMedicalLeaveBalance());

        // Calculate the number of pending medical leave days
        int pendingMedicalLeaveDays = leaveApplicationRepository
                .findByWorkerIdAndLeaveTypeAndApplicationStatus(
                        workerId,
                        LeaveType.MEDICAL,
                        LeaveApplication.ApplicationStatus.PENDING
                )
                .size(); // Assuming each pending application is for one leave day

        // Deduct pending leave from the approved balance to calculate the current available balance
        int availableMedicalLeave = approvedBalance - pendingMedicalLeaveDays;

        return Math.max(availableMedicalLeave, 0); // Ensure that balance is not negative
    }

    public int getOtherLeaveBalance(Long workerId) {
        // Fetch the most recent approved leave balance for other leave
        Optional<LeaveApplication> mostRecentApprovedOtherLeave = leaveApplicationRepository
                .findTopByWorkerIdAndLeaveTypeAndApplicationStatusOrderByLeaveSubmittedDateDescLeaveSubmittedTimeDesc(
                        workerId,
                        LeaveType.OTHERS,
                        LeaveApplication.ApplicationStatus.APPROVED
                );

        int approvedBalance = mostRecentApprovedOtherLeave
                .map(LeaveApplication::getOtherLeaveBalance)
                .orElse(getDynamicOtherLeaveBalance());

        // Calculate the number of pending other leave days
        int pendingOtherLeaveDays = leaveApplicationRepository
                .findByWorkerIdAndLeaveTypeAndApplicationStatus(
                        workerId,
                        LeaveType.OTHERS,
                        LeaveApplication.ApplicationStatus.PENDING
                )
                .size(); // Assuming each pending application is for one leave day

        // Deduct pending leave from the approved balance to calculate the current available balance
        int availableOtherLeave = approvedBalance - pendingOtherLeaveDays;

        return Math.max(availableOtherLeave, 0); // Ensure that balance is not negative
    }

}