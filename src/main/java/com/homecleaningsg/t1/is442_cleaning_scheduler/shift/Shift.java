package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
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
    private int shiftId;

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
    @Column(name = "sessionStart")
    private Timestamp sessionStart;

    @NonNull
    @Column(name = "sessionEnd")
    private Timestamp sessionEnd;

    @Column(name = "actualStart")
    private Timestamp actualStart;

    @Column(name = "actualEnd")
    private Timestamp actualEnd;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "workingStatus")
    private WorkingStatus workingStatus;

    public enum WorkingStatus {
        NOT_STARTED,
        WORKING,
        FINISHED
    }

    private boolean workerHasPendingLeave;

    @Lob
    @Column(name = "startAcknowledge")
    private byte[] startAcknowledge;

    @Lob
    @Column(name = "endAcknowledge")
    private byte[] endAcknowledge;

    public Shift(CleaningSession cleaningSession) {
        this.cleaningSession = cleaningSession;
        this.location = cleaningSession.getLocation();
        this.sessionDescription = cleaningSession.getSessionDescription();
        this.sessionStart = cleaningSession.getSessionStart();
        this.sessionEnd = cleaningSession.getSessionEnd();
        this.workingStatus = WorkingStatus.NOT_STARTED;
    }
}