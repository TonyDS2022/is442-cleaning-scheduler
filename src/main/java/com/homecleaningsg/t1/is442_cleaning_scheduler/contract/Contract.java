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
    private static final int MIN_DURATION = 60;
    private static final int MAX_DURATION = 240;

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

    @NonNull
    private Timestamp lastModified;

    // Derived field: getRate = price / sessionDurationMinutes
    public float getRate() {
        return price / sessionDurationMinutes;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "contractStatus", nullable = false)
    private ContractStatus contractStatus = ContractStatus.NOT_STARTED;

    public enum ContractStatus {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    // temp for retrieving all contracts by cleaningSessionIds
    @Getter
    @OneToMany(mappedBy = "contract")
    private List<CleaningSession> cleaningSessions;

    public Contract(Location location,
                    Long clientId,
                    @NonNull Timestamp contractStart,
                    @NonNull Timestamp contractEnd,
                    String contractComment,
                    float price,
                    int workersBudgeted,
                    int rooms,
                    String frequency,
                    int sessionDurationMinutes,
                    ContractStatus contractStatus) {
        this.location = location;
        this.clientId = clientId;
        this.contractStart = contractStart;
        this.contractEnd = contractEnd;
        this.contractComment = contractComment;
        this.price = price;
        this.workersBudgeted = workersBudgeted;
        this.rooms = rooms;
        this.frequency = frequency;
        setSessionDurationMinutes(sessionDurationMinutes); // Use custom setter for validation
        this.contractStatus = contractStatus;
    }

    public void setSessionDurationMinutes(int sessionDurationMinutes){
        if(sessionDurationMinutes < 60 || sessionDurationMinutes > 240){
            throw new IllegalArgumentException("Service hours must be between 1 and 4 hours.");
        }
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }
}