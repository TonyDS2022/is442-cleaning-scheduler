package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
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
    @JoinColumn(name = "clientSiteId", nullable = true)
    ClientSite clientSite;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "clientId",  nullable = true)
    @JsonBackReference("client-contract")
    private Client client;

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

    @NonNull
    private boolean isActive = true;

    // temp for retrieving all contracts by cleaningSessionIds
    @Getter
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("contract-cleaningSession")
    private List<CleaningSession> cleaningSessions = new ArrayList<>();

    public Contract(ClientSite clientSite,
                    Client client,
                    @NonNull Timestamp contractStart,
                    @NonNull Timestamp contractEnd,
                    String contractComment,
                    float price,
                    int workersBudgeted,
                    int rooms,
                    String frequency,
                    int sessionDurationMinutes,
                    ContractStatus contractStatus) {
        this.clientSite = clientSite;
        this.client = client;
        this.contractStart = contractStart;
        this.contractEnd = contractEnd;
        this.contractComment = contractComment;
        this.price = price;
        this.workersBudgeted = workersBudgeted;
        this.rooms = rooms;
        this.frequency = Frequency.valueOf(frequency);
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
    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }
}