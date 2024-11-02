package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import jakarta.persistence.*;
import lombok.*;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table(name = "CleaningSession")
public class CleaningSession {
    // use sequence generator for sessionId
    @Id
    @SequenceGenerator(
            name = "cleaning_session_sequence",
            sequenceName = "cleaning_session_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cleaning_session_sequence"
    )
    private Long cleaningSessionId;

    // refers to cleaningSessionId col to establish relationship with Shift START
    @OneToMany(mappedBy = "cleaningSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shift> shifts;

    @ManyToOne
    private Location location;

    @Column(name = "workersBudgeted")
    private int workersBudgeted;

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

    @NonNull
    @Column(name = "sessionDescription")
    private String sessionDescription;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sessionStatus")
    private sessionStatus sessionStatus;

    public enum sessionStatus {
        NOT_STARTED,
        WORKING,
        FINISHED
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "sessionRating")
    private Rating sessionRating;

    public enum Rating {
        BAD,
        POOR,
        AVERAGE,
        GOOD,
        EXCELLENT
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "planningStage")
    private PlanningStage planningStage;

    public enum PlanningStage {
        GREEN, /* if workersBudgeted == no. shifts that has a worker assigned */
        EMBER, /* if workersBudgeted == no. shifts that has a worker assinged but one or more of them has pending leave application */
        RED, /* if workersBudgeted < no. shifts that has a worker assigned */
    }

    @Column(name = "sessionFeedback")
    private String sessionFeedback;

    @ManyToOne
    @JoinColumn(name = "contractId", nullable = false)
    @JsonBackReference // prevent infinite recursion
    private Contract contract;

    @NonNull
    private boolean isActive = true;

    @NonNull
    private Timestamp lastModified;

    // New constructor
    public CleaningSession(LocalDate sessionStartDate,
                           LocalTime sessionStartTime,
                           LocalDate sessionEndDate,
                           LocalTime sessionEndTime,
                           String sessionDescription,
                           sessionStatus sessionStatus) {
        setSessionStartTime(sessionStartTime);
        setSessionEndTime(sessionEndTime);
        this.sessionStartDate = sessionStartDate;
        this.sessionEndDate = sessionEndDate;
        this.sessionEndTime = sessionEndTime;
        this.sessionDescription = sessionDescription;
        this.sessionStatus = sessionStatus;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }

    // setter with validation checks
    public void setSessionStartTime(LocalTime sessionStartTime){
        LocalTime MIN_START_TIME = CleaningSessionConfigLoader.MIN_START_TIME;
        LocalTime MAX_END_TIME = CleaningSessionConfigLoader.MAX_END_TIME;
        LocalTime START_LUNCH_TIME = CleaningSessionConfigLoader.START_LUNCH_TIME;
        LocalTime END_LUNCH_TIME = CleaningSessionConfigLoader.END_LUNCH_TIME;
        LocalTime START_DINNER_TIME = CleaningSessionConfigLoader.START_DINNER_TIME;
        LocalTime END_DINNER_TIME = CleaningSessionConfigLoader.END_DINNER_TIME;
        if (sessionStartTime.isBefore(MIN_START_TIME) || sessionStartTime.isAfter(MAX_END_TIME)){
            throw new IllegalArgumentException("Session time must be between 8am - 10pm.");
        }

        // Check if session overlaps with lunch time
        boolean overlapsLunch = sessionStartTime.isBefore(END_LUNCH_TIME) && sessionStartTime.isAfter(START_LUNCH_TIME);

        // Check if session overlaps with dinner time
        boolean overlapsDinner = sessionStartTime.isBefore(END_DINNER_TIME) && sessionStartTime.isAfter(START_DINNER_TIME);

        if (overlapsLunch || overlapsDinner) {
            throw new IllegalArgumentException("Session time must not overlap with lunch (12pm - 1pm) or dinner (5pm - 6pm) hours.");
        }

        this.sessionStartTime = sessionStartTime;
    }

    public void setSessionEndTime(LocalTime sessionEndTime){
        LocalTime MIN_START_TIME = CleaningSessionConfigLoader.MIN_START_TIME;
        LocalTime MAX_END_TIME = CleaningSessionConfigLoader.MAX_END_TIME;
        LocalTime START_LUNCH_TIME = CleaningSessionConfigLoader.START_LUNCH_TIME;
        LocalTime END_LUNCH_TIME = CleaningSessionConfigLoader.END_LUNCH_TIME;
        LocalTime START_DINNER_TIME = CleaningSessionConfigLoader.START_DINNER_TIME;
        LocalTime END_DINNER_TIME = CleaningSessionConfigLoader.END_DINNER_TIME;
        if (sessionEndTime.isBefore(MIN_START_TIME) || sessionEndTime.isAfter(MAX_END_TIME)){
            throw new IllegalArgumentException("Session time must be between 8am - 10pm.");
        }

        // Check if session overlaps with lunch time
        boolean overlapsLunch = sessionEndTime.isBefore(END_LUNCH_TIME) && sessionEndTime.isAfter(START_LUNCH_TIME);

        // Check if session overlaps with dinner time
        boolean overlapsDinner = sessionEndTime.isBefore(END_DINNER_TIME) && sessionEndTime.isAfter(START_DINNER_TIME);

        if (overlapsLunch || overlapsDinner) {
            throw new IllegalArgumentException("Session time must not overlap with lunch (12pm - 1pm) or dinner (5pm - 6pm) hours.");
        }

        this.sessionEndTime = sessionEndTime;
    }
}