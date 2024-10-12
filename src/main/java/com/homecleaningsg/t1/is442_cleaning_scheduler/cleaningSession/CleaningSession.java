package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.sessionTicket.SessionTicket;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

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
    @JsonBackReference // prevent infinite recursion
    private Contract contract;

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
    private int cleaningSessionId;

    // refers to cleaningSessionId col to establish relationship with SessionTicket START
    @OneToMany(mappedBy = "cleaningSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // prevent infinite recursion
    private List<SessionTicket> sessionTickets;

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