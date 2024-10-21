package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;



@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
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
    private String leaveType;
    private String fileName;
    @NonNull
    private OffsetDateTime affectedShiftStart;
    @NonNull
    private OffsetDateTime affectedShiftEnd;
    @NonNull
    private OffsetDateTime applicationSubmitted;
    @NonNull
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
}
