package com.homecleaningsg.t1.is442_cleaning_scheduler.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table(name = "job")
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
    @JsonIgnore
    private Long jobId;

    @NonNull
    private Long employeeId;

    @NonNull
    private LocalDateTime timeStamp;

    @NonNull
    private Double estimatedDuration;

    @NonNull
    private String status; // 'not started', 'in-progress', 'completed'

    @NonNull
    private String jobName;
}
