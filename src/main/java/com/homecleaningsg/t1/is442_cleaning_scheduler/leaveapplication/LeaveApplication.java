package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;



@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class LeaveApplication {
    @Id
    @SequenceGenerator(
            name = "leave_application_sequence",
            sequenceName = "leave_application_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "leave_application_sequence"
    )
    private Long leaveApplicationId;
    @NonNull
    private Long workerId;
    @NonNull
    private Long adminId;
    @NonNull
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;
    @JsonIgnore
    private String fileName;
    @JsonIgnore
    private String imageHash;

    @NonNull
    @Column(name = "leaveStartDate")
    private LocalDate leaveStartDate;

    @NonNull
    @Column(name = "leaveStartTime")
    private LocalTime leaveStartTime;

    @NonNull
    @Column(name = "leaveEndDate")
    private LocalDate leaveEndDate;

    @NonNull
    @Column(name = "leaveEndTime")
    private LocalTime leaveEndTime;

    @NonNull
    @Column(name = "leaveSubmittedDate")
    private LocalDate leaveSubmittedDate;

    @NonNull
    @Column(name = "leaveSubmittedTime")
    private LocalTime leaveSubmittedTime;

    @NonNull
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    enum ApplicationStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    @NonNull
    private Timestamp lastModified;

    private int medicalLeaveBalance;
    private int otherLeaveBalance;

    public LeaveApplication(
            Long workerId,
            Long adminId,
            LeaveType leaveType,
            String fileName,
            String imageHash,
            LocalDate leaveStartDate,
            LocalTime leaveStartTime,
            LocalDate leaveEndDate,
            LocalTime leaveEndTime,
            LocalDate leaveSubmittedDate,
            LocalTime leaveSubmittedTime,
            ApplicationStatus applicationStatus,
            int medicalLeaveBalance,
            int otherLeaveBalance
    ) {
        this.workerId = workerId;
        this.adminId = adminId;
        this.leaveType = leaveType;
        this.fileName = fileName;
        this.imageHash = imageHash;
        this.leaveStartDate = leaveStartDate;
        this.leaveStartTime = leaveStartTime;
        this.leaveEndDate = leaveEndDate;
        this.leaveEndTime = leaveEndTime;
        this.leaveSubmittedDate = leaveSubmittedDate;
        this.leaveSubmittedTime = leaveSubmittedTime;
        this.applicationStatus = applicationStatus;
        this.medicalLeaveBalance = medicalLeaveBalance;
        this.otherLeaveBalance = otherLeaveBalance;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }
}
