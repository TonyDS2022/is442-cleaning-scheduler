package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
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
    private Long contractId;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "locationId", nullable = false)
    private Location location;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clientId",  nullable = false)
    @JsonManagedReference
    private Client client;

    @NonNull
    @Column(name = "contractStart")
    private LocalDate contractStart;

    @NonNull
    @Column(name = "contractEnd")
    private LocalDate contractEnd;

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

    @NonNull
    private LocalDate creationDate;

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

    @NonNull
    private boolean isActive = true;

    // temp for retrieving all contracts by cleaningSessionIds
    @Getter
    @OneToMany(mappedBy = "contract")
    private List<CleaningSession> cleaningSessions;

    public Contract(Location location,
                    Client client,
                    @NonNull LocalDate contractStart,
                    @NonNull LocalDate contractEnd,
                    String contractComment,
                    float price,
                    int workersBudgeted,
                    int rooms,
                    String frequency,
                    int sessionDurationMinutes,
                    ContractStatus contractStatus) {
        this.location = location;
        this.client = client;
        this.contractStart = contractStart;
        this.contractEnd = contractEnd;
        this.contractComment = contractComment;
        this.price = price;
        this.workersBudgeted = workersBudgeted;
        this.rooms = rooms;
        this.frequency = frequency;
        setSessionDurationMinutes(sessionDurationMinutes); // Use custom setter for validation
        this.contractStatus = contractStatus;
        this.validateSessionDurationMinutes();
    }

    // Helper method for validation
    public void validateSessionDurationMinutes(){
        if(sessionDurationMinutes < ContractConfigLoader.MIN_SESSION_DURATION_MINUTES || sessionDurationMinutes > ContractConfigLoader.MAX_SESSION_DURATION_MINUTES){
            throw new IllegalArgumentException("Service hours must be between 1 and 4 hours.");
        }
    }

    @PrePersist
    protected void onCreate() {
        lastModified = new Timestamp(System.currentTimeMillis());
        LocalDate today = LocalDate.now();
        if (this.contractEnd.isBefore(today)) {
            this.contractStatus = ContractStatus.COMPLETED;
        } else if (this.contractStart.isBefore(today) || this.contractStart.isEqual(today)) {
            this.contractStatus = ContractStatus.IN_PROGRESS;
        } else {
            this.contractStatus = ContractStatus.NOT_STARTED;
        }
    }
    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }
}