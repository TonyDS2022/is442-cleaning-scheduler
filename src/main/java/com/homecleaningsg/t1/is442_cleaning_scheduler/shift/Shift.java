package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table(name = "Shift")
public class Shift {
    // reuse sequence generator of sessionId
    @Id
    @SequenceGenerator(
            name = "shift_sequence",
            sequenceName = "shift_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shift_sequence"
    )
    private Long shiftId;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Location location;

    private String sessionDescription;

    // refers to workerId col to establish relationship
    @ManyToOne
    @JoinColumn(name = "workerId")
    private Worker worker;

    // refers to cleaningSession to establish relationship
    @NonNull
    @ManyToOne
    @JsonBackReference
    private CleaningSession cleaningSession;

    @NonNull
    @Column(name = "sessionStartDate")
    private LocalDate sessionStartDate;

    @NonNull
    @Column(name = "sessionStartTime")
    private LocalTime sessionStartTime;

    @NonNull
    @Column(name = "sessionEndDate")
    private LocalDate sessionEndDate;

    @NonNull
    @Column(name = "sessionEndTime")
    private LocalTime sessionEndTime;

    @Column(name = "actualStartDate")
    private LocalDate actualStartDate;

    @Column(name = "actualStartTime")
    private LocalTime actualStartTime;

    @Column(name = "actualEndDate")
    private LocalDate actualEndDate;

    @Column(name = "actualEndTime")
    private LocalTime actualEndTime;

    @Column(name = "duration")
    private Long duration;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "workingStatus")
    private WorkingStatus workingStatus;

    public enum WorkingStatus {
        NOT_STARTED,
        WORKING,
        FINISHED,
        CANCELLED
    }

    private boolean workerHasPendingLeave;

    @Lob
    @Column(name = "startAcknowledge")
    private byte[] startAcknowledge;

    @Lob
    @Column(name = "endAcknowledge")
    private byte[] endAcknowledge;

    @NonNull
    private Timestamp lastModified;

    private LocalDate cancelledAt;

    public Shift(CleaningSession cleaningSession) {
        this.cleaningSession = cleaningSession;
        this.location = cleaningSession.getLocation();
        // Shifts share the same startDate, endDate, startTime, endTime as the cleaning session they are linked to
        this.sessionDescription = cleaningSession.getSessionDescription();
        this.sessionStartDate = cleaningSession.getSessionStartDate();
        this.sessionStartTime = cleaningSession.getSessionStartTime();
        this.sessionEndDate = cleaningSession.getSessionEndDate();
        this.sessionEndTime = cleaningSession.getSessionEndTime();
        this.workingStatus = WorkingStatus.NOT_STARTED;
    }

    @PrePersist
    @PreUpdate
    private void onUpdate() {
        this.lastModified = new Timestamp(System.currentTimeMillis());
        if (actualStartDate != null && actualStartTime != null && actualEndDate != null && actualEndTime != null) {
            LocalDateTime startDateTime = LocalDateTime.of(actualStartDate, actualStartTime);
            LocalDateTime endDateTime = LocalDateTime.of(actualEndDate, actualEndTime);
            this.duration = Duration.between(startDateTime, endDateTime).toHours();
        } else {
            this.duration = 0L;
        }
    }
}