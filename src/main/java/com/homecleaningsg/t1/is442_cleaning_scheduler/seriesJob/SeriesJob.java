package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.series.Series;
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
@IdClass(SeriesJobId.class)
@Table(name = "SeriesJob")
public class SeriesJob {
    @Id
    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    @JsonBackReference
    private Series series;

    @Id
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

    public SeriesJob(Series series, int jobId, Timestamp jobStart, Timestamp jobEnd, String jobDescription, JobStatus jobStatus) {
        this.series = series;
        this.jobId = jobId;
        this.jobStart = jobStart;
        this.jobEnd = jobEnd;
        this.jobDescription = jobDescription;
        this.jobStatus = jobStatus;
    }
}