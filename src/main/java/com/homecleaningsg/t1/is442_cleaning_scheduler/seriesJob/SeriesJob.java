package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;

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
@Table(name = "SeriesJob")
public class SeriesJob {
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

    @Column(name = "series_id")
    private int seriesId;

    @Column(name = "job_id")
    private int jobId;

    @NonNull
    @Column(name = "job_start")
    private Timestamp jobStart;

    @NonNull
    @Column(name = "job_end")
    private Timestamp jobEnd;

    @NonNull
    @Column(name = "job_description")
    private String jobDescription;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "job_status")
    private JobStatus jobStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating")
    private Rating rating;

    @Column(name = "job_feedback")
    private String jobFeedback;

    // Enum classes for jobStatus and rating
    public enum JobStatus {
        NOT_STARTED,
        WORKING,
        FINISHED
    }

    public enum Rating {
        BAD,
        POOR,
        AVERAGE,
        GOOD,
        EXCELLENT
    }
}
