package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class LeaveApplicationConfig implements CommandLineRunner {

    private final LeaveApplicationRepository leaveApplicationRepository;

    public LeaveApplicationConfig(LeaveApplicationRepository leaveApplicationRepository) {
        this.leaveApplicationRepository = leaveApplicationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Generate sample data for leave applications
        LeaveApplication leaveApp1 = new LeaveApplication(
                1L,  // workerId
                2L,  // adminId
                LeaveType.MEDICAL,
                "fake-medical-cert-001.pdf",  // fileName
                OffsetDateTime.of(LocalDate.of(2024, 11, 6), LocalTime.MIDNIGHT, ZoneOffset.UTC),  // affectedShiftStart
                OffsetDateTime.of(LocalDate.of(2024, 11, 11), LocalTime.MIDNIGHT, ZoneOffset.UTC),  // affectedShiftEnd
                OffsetDateTime.now().minusDays(12),  // applicationSubmitted
                ApplicationStatus.PENDING  // applicationStatus
        );

        LeaveApplication leaveApp2 = new LeaveApplication(
                1L,
                3L,
                LeaveType.OTHERS,
                OffsetDateTime.now().plusDays(2),
                OffsetDateTime.now().plusDays(7),
                OffsetDateTime.now(),
                ApplicationStatus.PENDING
        );

        LeaveApplication leaveApp3 = new LeaveApplication(
                1L,
                4L,
                LeaveType.OTHERS,
                OffsetDateTime.now().minusDays(3),
                OffsetDateTime.now().minusDays(1),
                OffsetDateTime.now().minusDays(4),
                ApplicationStatus.REJECTED
        );

        LeaveApplication leaveApp4 = new LeaveApplication(
                1L,
                5L,
                LeaveType.OTHERS,
                OffsetDateTime.now().minusDays(20),
                OffsetDateTime.now().minusDays(15),
                OffsetDateTime.now().minusDays(25),
                ApplicationStatus.APPROVED
        );

        // Save the sample data to the database
        leaveApplicationRepository.saveAll(List.of(leaveApp1, leaveApp2, leaveApp3, leaveApp4));
    }
}
