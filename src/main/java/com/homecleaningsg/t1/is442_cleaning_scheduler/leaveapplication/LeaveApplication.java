package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.*;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "LeaveApplication")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "leaveApplicationId")
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

    private int medicalLeaveBalance;
    private int otherLeaveBalance;
  
    @OneToMany(mappedBy = "leaveApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // to prevent infinite recursion
    private List<Worker> workers;

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
 
    public LeaveApplication(Long workerId, Long adminId, LeaveType leaveType, String fileName, OffsetDateTime affectedShiftStart, OffsetDateTime affectedShiftEnd, OffsetDateTime applicationSubmitted, ApplicationStatus applicationStatus) {
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
}
