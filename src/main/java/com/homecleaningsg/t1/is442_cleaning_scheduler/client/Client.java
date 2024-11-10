package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
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
@Table
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

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientSite> clientSites;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonManagedReference("client-contract")
    private List<Contract> contracts;

    public Client(
            String name,
            String phone,
            boolean isActive
    ) {
        this.name = name;
        this.phone = phone;
        this.isActive = isActive;
        this.contracts = new ArrayList<>();
        this.clientSites = new ArrayList<>();
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
