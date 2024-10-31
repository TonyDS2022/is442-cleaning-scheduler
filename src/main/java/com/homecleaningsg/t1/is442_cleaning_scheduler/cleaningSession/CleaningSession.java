package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import jakarta.persistence.*;
import lombok.*;

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
    // refers to Contract contractId col to establish relationship

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

    // @Transient prevents getters, will need to create manually
    // @Transient
    @Enumerated(EnumType.STRING)
    @Column(name = "planningStage")
    private PlanningStage planningStage;

    public enum PlanningStage {
        GREEN, /* if workersBudgeted == no. shifts that has a worker assigned */
        EMBER, /* if workersBudgeted == no. shifts that has a worker assigned but one or more of them has pending leave application */
        RED, /* if workersBudgeted < no. shifts that has a worker assigned */
    }

    @Column(name = "sessionFeedback")
    private String sessionFeedback;

    @ManyToOne
    @JoinColumn(name = "contractId", nullable = false)
    @JsonBackReference // prevent infinite recursion
    private Contract contract;

    // New constructor
    public CleaningSession(Contract contract,
                           LocalDate sessionStartDate,
                           LocalTime sessionStartTime,
                           LocalDate sessionEndDate,
                           LocalTime sessionEndTime,
                           String sessionDescription,
                           sessionStatus sessionStatus
                           ) {
        this.contract = contract;
        this.sessionStartDate = sessionStartDate;
        this.sessionStartTime = sessionStartTime;
        this.sessionEndDate = sessionEndDate;
        this.sessionEndTime = sessionEndTime;
        this.sessionDescription = sessionDescription;
        this.sessionStatus = sessionStatus;
        this.workersBudgeted = contract.getWorkersBudgeted();
        this.location = contract.getLocation();
    }

    // // Update PlanningStage based on the shift's number of workers assigned and pending leave
    // public PlanningStage getPlanningStage() {
    //     boolean hasPendingLeave = false;
    //     int assignedWorkers = 0;
    //
    //     for (Shift shift : shifts) {
    //         if (shift.isWorkerHasPendingLeave()) {
    //             hasPendingLeave = true;
    //         }
    //         if (shift.getWorker() != null) {
    //             assignedWorkers++;
    //         }
    //     }
    //
    //     if (assignedWorkers < workersBudgeted) {
    //         return PlanningStage.RED;
    //     } else if (hasPendingLeave) {
    //         return PlanningStage.EMBER;
    //     } else {
    //         return PlanningStage.GREEN;
    //     }
    // }
}