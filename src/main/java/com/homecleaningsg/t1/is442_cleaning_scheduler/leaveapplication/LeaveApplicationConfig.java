package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class LeaveApplicationConfig implements CommandLineRunner {

    private final LeaveApplicationRepository leaveApplicationRepository;

    public LeaveApplicationConfig(LeaveApplicationRepository leaveApplicationRepository) {
        this.leaveApplicationRepository = leaveApplicationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Generate sample data for leave applications using the constructor
        LeaveApplication leaveApp1 = new LeaveApplication(
                1L,
                2L,
                LeaveType.MEDICAL,
                "fake-medical-cert-001.pdf",
                "fake-image-hash-001",
                OffsetDateTime.now().minusDays(10),
                OffsetDateTime.now().minusDays(5),
                OffsetDateTime.now().minusDays(12),
                ApplicationStatus.APPROVED,
                5,  // medicalLeaveBalance
                10  // otherLeaveBalance
        );

        LeaveApplication leaveApp2 = new LeaveApplication(
                1L,
                3L,
                LeaveType.OTHERS,
                null,  // fileName is null since it's not a medical leave
                null,
                OffsetDateTime.now().plusDays(2),
                OffsetDateTime.now().plusDays(7),
                OffsetDateTime.now(),
                ApplicationStatus.PENDING,
                5,  // medicalLeaveBalance
                10  // otherLeaveBalance
        );

        LeaveApplication leaveApp3 = new LeaveApplication(
                1L,
                4L,
                LeaveType.OTHERS,
                null,  // fileName is null since it's not a medical leave
                null,
                OffsetDateTime.now().minusDays(3),
                OffsetDateTime.now().minusDays(1),
                OffsetDateTime.now().minusDays(4),
                ApplicationStatus.REJECTED,
                5,  // medicalLeaveBalance
                10  // otherLeaveBalance
        );

        leaveApplicationRepository.saveAll(List.of(leaveApp1, leaveApp2, leaveApp3));
    }
}
