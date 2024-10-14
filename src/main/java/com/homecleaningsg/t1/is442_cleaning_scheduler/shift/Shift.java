package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
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

    // refers to workerId col to establish relationship
    @NonNull
    @ManyToOne
    @JoinColumn(name = "workerId", nullable = false)
    @JsonManagedReference // works with @JsonBackReference in Worker to prevent infinite recursion
    private Worker worker;

    // refers to cleaningSession to establish relationship
    @NonNull
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "contractId", referencedColumnName = "contractId"),
            @JoinColumn(name = "cleaningSessionId", referencedColumnName = "cleaningSessionId")
    })
    private CleaningSession cleaningSession;

    @NonNull
    @Column(name = "actualStart")
    private Timestamp actualStart;

    @NonNull
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

    @Lob
    @Column(name = "startAcknowledge")
    private byte[] startAcknowledge;

    @Lob
    @Column(name = "endAcknowledge")
    private byte[] endAcknowledge;
}