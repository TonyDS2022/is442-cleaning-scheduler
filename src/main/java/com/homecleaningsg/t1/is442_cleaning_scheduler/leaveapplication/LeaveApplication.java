package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.homecleaningsg.t1.is442_cleaning_scheduler.DateTimeUtils;
import com.homecleaningsg.t1.is442_cleaning_scheduler.admin.Admin;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

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
    @ManyToOne(cascade = CascadeType.DETACH)
    @JsonBackReference("worker-leaveApplication")
    private Worker worker;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Admin admin;

    @NonNull
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

//    @JsonIgnore
//    private String fileName;
//
//    @JsonIgnore
//    private String imageHash;

    @NonNull
    @Column(name = "leaveStartDate")
    private LocalDate leaveStartDate;

    @NonNull
    @Column(name = "leaveEndDate")
    private LocalDate leaveEndDate;

    @NonNull
    @Column(name = "leaveSubmittedDate")
    private LocalDate leaveSubmittedDate;

    @NonNull
    @Column(name = "leaveSubmittedTime")
    private LocalTime leaveSubmittedTime;

    @NonNull
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    public enum ApplicationStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public enum LeaveType {
        MEDICAL,
        ANNUAL
    }

    public Long leaveDurationDays;

    @NonNull
    private Timestamp lastModified;

    public LeaveApplication(
            Worker worker,
            LeaveType leaveType,
            LocalDate leaveStartDate,
            LocalDate leaveEndDate
    ) {
        this.worker = worker;
        this.admin = worker.getSupervisor();
        this.leaveType = leaveType;
        this.leaveStartDate = leaveStartDate;
        this.leaveEndDate = leaveEndDate;
        this.leaveSubmittedDate = LocalDate.now();
        this.leaveSubmittedTime = LocalTime.now();
        this.applicationStatus = ApplicationStatus.PENDING;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        leaveDurationDays = DateTimeUtils.numberOfWorkingDaysBetween(leaveStartDate, leaveEndDate);
        lastModified = new Timestamp(System.currentTimeMillis());
    }
}
