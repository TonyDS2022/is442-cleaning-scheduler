package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
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
@Table(name = "Contract")
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "contractId")
public class Contract {
    @Id
    @SequenceGenerator(
            name = "contract_sequence",
            sequenceName = "contract_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "contract_sequence"
    )
    private Long contractId;

    // @ManyToOne
    // @JoinColumn(name = "workerId", nullable = false)
    // @JsonManagedReference
    // private Worker worker;

    @ManyToOne
    private Location location;

    @Column(name = "clientId")
    private Long clientId;

    @NonNull
    @Column(name = "contractStart")
    private Timestamp contractStart;

    @NonNull
    @Column(name = "contractEnd")
    private Timestamp contractEnd;

    @Column(name = "contractComment")
    private String contractComment;

    @Column(name = "isOngoing")
    private boolean isOngoing;

    @Column(name = "price")
    private float price;

    @Column(name = "workersBudgeted")
    private int workersBudgeted;

    @Column(name = "rooms")
    private int rooms;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency")
    private Frequency frequency;

    public enum Frequency {
        DAILY,
        WEEKLY,
        BIWEEKLY,
        MONTHLY,
        BIMONTHLY,
        QUARTERLY,
        ANNUALLY
    }

    @Column(name = "sessionDurationMinutes")
    private int sessionDurationMinutes;

    // Derived field: getRate = price / sessionDurationMinutes
    public float getRate() {
        return price / sessionDurationMinutes;
    }

    // temp for retrieving all contracts by cleaningSessionIds
    @OneToMany(mappedBy = "contract")
    @JsonManagedReference
    private List<CleaningSession> cleaningSessions;
}