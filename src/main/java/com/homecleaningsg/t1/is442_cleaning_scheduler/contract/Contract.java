package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
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
    private int contractId;

    @ManyToOne
    private Location location;

    @Column(name = "acctId")
    private int acctId;

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

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "sessionDurationMinutes")
    private int sessionDurationMinutes;

    // Derived field: getRate = price / sessionDurationMinutes
    public float getRate() {
        return price / sessionDurationMinutes;
    }

    // temp for retrieving all contracts by cleaningSessionIds
    @Getter
    @OneToMany(mappedBy = "contract")
    private List<CleaningSession> cleaningSessions;
}