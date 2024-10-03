package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
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
@IdClass(CleaningSessionId.class)
@Table(name = "CleaningSession")
public class CleaningSession {
    // refers to Contract contractId col to establish relationship
    @Id
    @ManyToOne
    @JoinColumn(name = "contractId", nullable = false)
    @JsonBackReference
    private Contract contract;

    // use sequence generator for sessionId
    @Id
    @SequenceGenerator(
            name = "session_sequence",
            sequenceName = "session_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "session_sequence"
    )
    private int sessionId;

    @NonNull
    @Column(name = "sessionStart")
    private Timestamp sessionStart;

    @NonNull
    @Column(name = "sessionEnd")
    private Timestamp sessionEnd;

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

    @Column(name = "sessionFeedback")
    private String sessionFeedback;

    // New constructor
    public CleaningSession(Contract contract, Timestamp sessionStart, Timestamp sessionEnd, String sessionDescription, sessionStatus sessionStatus) {
        this.contract = contract;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
        this.sessionDescription = sessionDescription;
        this.sessionStatus = sessionStatus;
    }
}