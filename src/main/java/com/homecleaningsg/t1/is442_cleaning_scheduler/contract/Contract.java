package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
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

    // 1 contract may have multiple cleaningSessions
    // cascade applies all changes from Contract to associated cleaningSessions
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CleaningSession> cleaningSessionId;

    @Column(name = "geolocationId")
    private int geolocationId;

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

    @Column(name = "workersAssigned")
    private int workersAssigned;

    @Column(name = "rooms")
    private int rooms;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "sessionDuration")
    private int sessionDuration;

    // Derived field: hourlyRate
    public float getHourlyRate() {
        return price / sessionDuration;
    }
}