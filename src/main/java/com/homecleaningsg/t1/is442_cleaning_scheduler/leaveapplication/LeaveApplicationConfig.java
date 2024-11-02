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
                "fake-medical-cert-001.jpg",
                "fake-image-hash-001",
                OffsetDateTime.now().minusDays(30),
                OffsetDateTime.now().minusDays(25),
                OffsetDateTime.now().minusDays(20),
                ApplicationStatus.APPROVED,
                4,  // medicalLeaveBalance
                10  // otherLeaveBalance
        );

        LeaveApplication leaveApp2 = new LeaveApplication(
                2L,
                3L,
                LeaveType.OTHERS,
                null,  // fileName is null since it's not a medical leave
                null,
                OffsetDateTime.now().minusDays(18),
                OffsetDateTime.now().minusDays(15),
                OffsetDateTime.now().minusDays(10),
                ApplicationStatus.PENDING,
                4,  // medicalLeaveBalance
                10  // otherLeaveBalance
        );

        LeaveApplication leaveApp3 = new LeaveApplication(
                1L,
                2L,
                LeaveType.OTHERS,
                null,  // fileName is null since it's not a medical leave
                null,
                OffsetDateTime.now().minusDays(9),
                OffsetDateTime.now().minusDays(8),
                OffsetDateTime.now().minusDays(6),
                ApplicationStatus.REJECTED,
                4,  // medicalLeaveBalance
                10  // otherLeaveBalance
        );

        LeaveApplication leaveApp4 = new LeaveApplication(
                1L,
                2L,
                LeaveType.MEDICAL,
                "fake-medical-cert-002.jpg",  // fileName is null since it's not a medical leave
                "fake-image-hash-002",
                OffsetDateTime.now().minusDays(9),
                OffsetDateTime.now().minusDays(8),
                OffsetDateTime.now().minusDays(6),
                ApplicationStatus.APPROVED,
                3,  // medicalLeaveBalance
                10  // otherLeaveBalance
        );

        LeaveApplication leaveApp5 = new LeaveApplication(
                1L,
                2L,
                LeaveType.MEDICAL,
                "fake-medical-cert-003.jpg",  // fileName is null since it's not a medical leave
                "fake-image-hash-003",
                OffsetDateTime.now().minusDays(5),
                OffsetDateTime.now().minusDays(4),
                OffsetDateTime.now().minusDays(3),
                ApplicationStatus.APPROVED,
                2,  // medicalLeaveBalance
                10  // otherLeaveBalance
        );

        LeaveApplication leaveApp6 = new LeaveApplication(
                1L,
                2L,
                LeaveType.MEDICAL,
                "fake-medical-cert-004.jpg",  // fileName is null since it's not a medical leave
                "fake-image-hash-004",
                OffsetDateTime.now().minusDays(2),
                OffsetDateTime.now().minusDays(1),
                OffsetDateTime.now(),
                ApplicationStatus.APPROVED,
                1,  // medicalLeaveBalance
                10  // otherLeaveBalance
        );

        LeaveApplication leaveApp7 = new LeaveApplication(
                3L,
                2L,
                LeaveType.OTHERS,
                null,  // fileName is null since it's not a medical leave
                null,
                OffsetDateTime.now().plusDays(6),
                OffsetDateTime.now().plusDays(8),
                OffsetDateTime.now().plusDays(10),
                ApplicationStatus.APPROVED,
                5,  // medicalLeaveBalance
                9  // otherLeaveBalance
        );

        LeaveApplication leaveApp8 = new LeaveApplication(
                3L,
                3L,
                LeaveType.OTHERS,
                null,  // fileName is null since it's not a medical leave
                null,
                OffsetDateTime.now().plusDays(12),
                OffsetDateTime.now().plusDays(15),
                OffsetDateTime.now().plusDays(20),
                ApplicationStatus.APPROVED,
                5,  // medicalLeaveBalance
                8  // otherLeaveBalance
        );

        leaveApplicationRepository.saveAll(List.of(leaveApp1, leaveApp2, leaveApp3, leaveApp4, leaveApp5, leaveApp6, leaveApp7, leaveApp8));
    }
}
