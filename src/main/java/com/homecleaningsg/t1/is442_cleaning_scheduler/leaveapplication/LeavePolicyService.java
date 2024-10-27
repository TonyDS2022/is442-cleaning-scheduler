package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import lombok.*;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class LeavePolicyService {

    private final LeaveApplicationRepository leaveApplicationRepository;

    // Placeholder method to interact with AdminLeaveApprovalService for dynamic leave balance management
    public int getDynamicMedicalLeaveBalance() {
        // Call AdminLeaveApprovalService (not yet implemented) to get the updated medical leave balance
        return 5; // Placeholder value until integration with adminLeaveApprovalService
    }

    public int getDynamicOtherLeaveBalance() {
        // Call AdminLeaveApprovalService (not yet implemented) to get the updated medical leave balance
        return 10; // Placeholder value until integration with adminLeaveApprovalService
    }

    public int getMedicalLeaveBalance(Long workerId) {
        // Fetch the leave balance based on HR-configured policies or database
        Optional<LeaveApplication> mostRecentApprovedMedicalLeave = leaveApplicationRepository.findTopByWorkerIdAndLeaveTypeAndApplicationStatusOrderByApplicationSubmittedDesc(workerId, LeaveType.MEDICAL, ApplicationStatus.APPROVED);
        if (mostRecentApprovedMedicalLeave.isEmpty() || mostRecentApprovedMedicalLeave.get().getApplicationSubmitted().getYear() != OffsetDateTime.now().getYear()) {
            return getDynamicMedicalLeaveBalance(); // Default medical leave balance
        }
        return mostRecentApprovedMedicalLeave.get().getMedicalLeaveBalance();
    }

    public int getOtherLeaveBalance(Long workerId) {
        // Fetch the leave balance based on HR-configured policies or database
        Optional<LeaveApplication> mostRecentApprovedOtherLeave = leaveApplicationRepository.findTopByWorkerIdAndLeaveTypeAndApplicationStatusOrderByApplicationSubmittedDesc(workerId, LeaveType.OTHERS, ApplicationStatus.APPROVED);
        if (mostRecentApprovedOtherLeave.isEmpty() || mostRecentApprovedOtherLeave.get().getApplicationSubmitted().getYear() != OffsetDateTime.now().getYear()) {
            return getDynamicOtherLeaveBalance(); // Default other leave balance
        }
        return mostRecentApprovedOtherLeave.get().getOtherLeaveBalance();
    }
}