package com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table
public class ClientSite {
    @Id
    @SequenceGenerator(
            name = "client_site_sequence",
            sequenceName = "client_site_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_site_sequence"
    )
    @JsonIgnore
    private Long clientSiteId;

    @JsonBackReference("client-clientSite")
    @ManyToOne
    @JsonIgnore
    private Client client;

    @NonNull
    private String streetAddress;

    @NonNull
    private String postalCode;

    @NonNull
    private String unitNumber;

    @NonNull
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @NonNull
    @JsonIgnore
    private Timestamp lastModified;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }

    public ClientSite(
            Client client,
            String streetAddress,
            String postalCode,
            String unitNumber,
            Location location
    ) {
        this.client = client;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.unitNumber = unitNumber;
        this.location = location;
        client.addClientSite(this);
    }

    public boolean isSameSite(ClientSite clientSite) {
        return this.streetAddress.equals(clientSite.getStreetAddress()) && this.postalCode.equals(clientSite.getPostalCode()) && this.unitNumber.equals(clientSite.getUnitNumber());
    }
}

