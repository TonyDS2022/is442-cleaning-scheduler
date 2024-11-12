package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private LocalDate contractStart;

    @NonNull
    @Column(name = "sessionStartTime")
    private LocalTime sessionStartTime;

    @NonNull
    @Column(name = "contractEnd")
    private LocalDate contractEnd;

    @NonNull
    @Column(name = "sessionEndTime")
    private LocalTime sessionEndTime;

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

    @Column(name = "sessionDurationHours")
    private int sessionDurationHours;

    @NonNull
    private Timestamp lastModified;

    @NonNull
    private LocalDate creationDate;

    // Derived field: getRate = price / sessionDurationHours
    public float getRate() {
        return price / sessionDurationHours;
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
                    @NonNull LocalDate contractStart,
                    @NonNull LocalDate contractEnd,
                    @NonNull LocalTime sessionStartTime,
                    @NonNull LocalTime sessionEndTime,
                    String contractComment,
                    float price,
                    int workersBudgeted,
                    int rooms,
                    String frequency,
                    ContractStatus contractStatus) {
        this.clientSite = clientSite;
        this.client = client;
        this.contractStart = contractStart;
        this.contractEnd = contractEnd;
        this.contractComment = contractComment;
        this.sessionStartTime = sessionStartTime;
        this.sessionEndTime = sessionEndTime;
        this.price = price;
        this.workersBudgeted = workersBudgeted;
        this.rooms = rooms;
        this.frequency = Frequency.valueOf(frequency);
        this.contractStatus = contractStatus;
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

        // Check that start time is before end time
        if (sessionStartTime.isAfter(sessionEndTime)) {
            throw new IllegalArgumentException("Session start time must be before session end time.");
        }
        // Check that session time is within 8am - 10pm
        if (sessionStartTime.isBefore(ContractConfigLoader.MIN_START_TIME)
                || sessionStartTime.isAfter(ContractConfigLoader.MAX_END_TIME)
                || sessionEndTime.isBefore(ContractConfigLoader.MIN_START_TIME)
                || sessionEndTime.isAfter(ContractConfigLoader.MAX_END_TIME)) {
            throw new IllegalArgumentException("Session time must be between 8am - 10pm.");
        }
        // Check that session time does not overlap with lunch or dinner hours
        if (sessionStartTime.isAfter(ContractConfigLoader.START_LUNCH_TIME)
                && sessionStartTime.isBefore(ContractConfigLoader.END_LUNCH_TIME)
                || sessionEndTime.isAfter(ContractConfigLoader.START_LUNCH_TIME)
                && sessionEndTime.isBefore(ContractConfigLoader.END_LUNCH_TIME)
                || sessionStartTime.isAfter(ContractConfigLoader.START_DINNER_TIME)
                && sessionStartTime.isBefore(ContractConfigLoader.END_DINNER_TIME)
                || sessionEndTime.isAfter(ContractConfigLoader.START_DINNER_TIME)
                && sessionEndTime.isBefore(ContractConfigLoader.END_DINNER_TIME)) {
            throw new IllegalArgumentException("Session time must not overlap with lunch (12pm - 1pm) or dinner (5pm - 6pm) hours.");
        }

        sessionDurationHours = (int) Duration.between(sessionStartTime, sessionEndTime).toHours();
        if (sessionDurationHours < ContractConfigLoader.MIN_SESSION_DURATION_HOURS
                || sessionDurationHours > ContractConfigLoader.MAX_SESSION_DURATION_HOURS) {
            throw new IllegalArgumentException("Service hours must be between 1 and 4 hours.");
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }
}