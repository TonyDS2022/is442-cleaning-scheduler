package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.*;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import jakarta.persistence.*;
import lombok.*;
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

    @OneToMany(mappedBy = "leaveApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // to prevent infinite recursion
    private List<Worker> workers;

    public LeaveApplication(Long workerId, Long adminId, LeaveType leaveType, String fileName, OffsetDateTime affectedShiftStart, OffsetDateTime affectedShiftEnd, OffsetDateTime applicationSubmitted, ApplicationStatus applicationStatus) {
        this.workerId = workerId;
        this.adminId = adminId;
        this.leaveType = leaveType;
        this.fileName = fileName;
        this.affectedShiftStart = affectedShiftStart;
        this.affectedShiftEnd = affectedShiftEnd;
        this.applicationSubmitted = applicationSubmitted;
        this.applicationStatus = applicationStatus;
    }
}
