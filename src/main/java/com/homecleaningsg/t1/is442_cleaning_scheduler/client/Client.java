package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
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

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Getter
    @OneToMany(mappedBy = "client")
    private List<Contract> contracts;

    public Client(String name,
                           String phone,
                           boolean isActive,
                           Location location,
                            LocalDate joinDate) {
        this.name = name;
        this.phone = phone;
        this.isActive = isActive;
        this.location = location;
        this.joinDate = joinDate;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }

}
