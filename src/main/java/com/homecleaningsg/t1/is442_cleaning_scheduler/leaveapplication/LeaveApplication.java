package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;



@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private Long applicationId;

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
    private OffsetDateTime affectedShiftStart;

    @NonNull
    private OffsetDateTime affectedShiftEnd;

    @NonNull
    private OffsetDateTime applicationSubmitted;

    @NonNull
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    private int medicalLeaveBalance;

    private int otherLeaveBalance;

    // Full constructor for including optional fields
    public LeaveApplication(@NonNull Long workerId,
                            @NonNull Long adminId,
                            @NonNull LeaveType leaveType,
                            String fileName,
                            String imageHash,
                            @NonNull OffsetDateTime affectedShiftStart,
                            @NonNull OffsetDateTime affectedShiftEnd,
                            @NonNull OffsetDateTime applicationSubmitted,
                            @NonNull ApplicationStatus applicationStatus,
                            int medicalLeaveBalance,
                            int otherLeaveBalance) {
        this.workerId = workerId;
        this.adminId = adminId;
        this.leaveType = leaveType;
        this.fileName = fileName;
        this.imageHash = imageHash;
        this.affectedShiftStart = affectedShiftStart;
        this.affectedShiftEnd = affectedShiftEnd;
        this.applicationSubmitted = applicationSubmitted;
        this.applicationStatus = applicationStatus;
        this.medicalLeaveBalance = medicalLeaveBalance;
        this.otherLeaveBalance = otherLeaveBalance;
    }
}