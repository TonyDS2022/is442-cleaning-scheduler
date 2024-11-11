package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table(name = "Client")
public class Client {
    @Id
    @SequenceGenerator(
            name = "client_sequence",
            sequenceName = "client_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_sequence"
    )
    private Long clientId;

    @NonNull
    private String name;

    @NonNull
    private String phone;

    @NonNull
    private boolean isActive = true;

    @NonNull
    private Timestamp lastModified;

    private LocalDate deactivatedAt;

    @NonNull
    private LocalDate joinDate;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientSite> clientSites = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("client-contract")
    private List<Contract> contracts = new ArrayList<>();

    public Client(
            String name,
            String phone,
            boolean isActive,
            LocalDate joinDate
    ) {
        this.name = name;
        this.phone = phone;
        this.isActive = isActive;
        this.contracts = new ArrayList<>();
        this.clientSites = new ArrayList<>();
        this.joinDate = joinDate;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }

    public void addContract(Contract contract) {
        contracts.add(contract);
        contract.setClient(this);
    }

    public void addClientSite(ClientSite clientSite) {
        clientSites.add(clientSite);
        clientSite.setClient(this);
    }

}
