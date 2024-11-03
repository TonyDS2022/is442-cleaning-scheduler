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
                "hash1",  // imageHash
                LocalDate.of(2024, 11, 1),  // leaveStartDate
                LocalTime.MIDNIGHT,  // leaveStartTime
                LocalDate.of(2024, 11, 1),  // leaveEndDate
                LocalTime.MIDNIGHT,  // leaveEndTime
                LocalDate.of(2024, 11, 6),  // leaveSubmittedDate
                LocalTime.of(8, 30),  // leaveSubmittedTime
                LeaveApplication.ApplicationStatus.APPROVED,  // applicationStatus
                10,  // medicalLeaveBalance
                5  // otherLeaveBalance
        );

        LeaveApplication leaveApp2 = new LeaveApplication(
                1L,
                3L,
                LeaveType.OTHERS,
                null,  // fileName
                "hash2",  // imageHash
                LocalDate.now().plusDays(2),
                LocalTime.MIDNIGHT,
                LocalDate.now().plusDays(7),
                LocalTime.MIDNIGHT,
                LocalDate.now(),
                LocalTime.now(),
                LeaveApplication.ApplicationStatus.PENDING,
                10,
                5
        );

        LeaveApplication leaveApp3 = new LeaveApplication(
                1L,
                4L,
                LeaveType.OTHERS,
                null,  // fileName
                "hash3",  // imageHash
                LocalDate.now().minusDays(3),
                LocalTime.MIDNIGHT,
                LocalDate.now().minusDays(1),
                LocalTime.MIDNIGHT,
                LocalDate.now().minusDays(4),
                LocalTime.now().minusHours(4),
                LeaveApplication.ApplicationStatus.REJECTED,
                10,
                5
        );

        // Save the sample data to the database
        leaveApplicationRepository.saveAll(List.of(leaveApp1, leaveApp2, leaveApp3));
    }
}
