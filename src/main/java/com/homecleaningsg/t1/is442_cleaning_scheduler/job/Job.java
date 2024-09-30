package com.homecleaningsg.t1.is442_cleaning_scheduler.job;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table(name = "Job")
public class Job {
    @Id
    @SequenceGenerator(
            name = "job_sequence",
            sequenceName = "job_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "job_sequence"
    )

    @Column(name = "job_id")
    private int jobId;

    @NonNull
    @Column(name = "package_start")
    private Timestamp packageStart;

    @NonNull
    @Column(name = "package_end")
    private Timestamp packageEnd;

    @NonNull
    @Column(name = "employee_id")
    private int employeeId;
}