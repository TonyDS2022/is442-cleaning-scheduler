package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

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
    private Long acctId;
    @NonNull
    private String leaveType;
    private String fileName;
    @NonNull
    private LocalDateTime startShiftDate;
    @NonNull
    private LocalDateTime endShiftDate;
}
