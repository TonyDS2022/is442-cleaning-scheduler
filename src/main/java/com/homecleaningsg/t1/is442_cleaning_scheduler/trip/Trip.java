package com.homecleaningsg.t1.is442_cleaning_scheduler.trip;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table
public class Trip {
    @Id
    @SequenceGenerator(
            name = "trip_sequence",
            sequenceName = "trip_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "trip_sequence"
    )
    @JsonIgnore
    private Long tripId;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "originId", referencedColumnName = "locationId")
    private Location origin;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "destinationId", referencedColumnName = "locationId")
    private Location destination;

    private double tripDurationSeconds;
    private double tripDistanceMeters;

    private double euclideanDistanceKm;

    public void setEuclideanDistanceKm() {
        this.euclideanDistanceKm = origin.getEuclideanDistanceKmFrom(destination);
    }

    public Trip() {
        this.tripDurationSeconds = 0;
        this.tripDistanceMeters = 0;
        this.euclideanDistanceKm = 0;
    }

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        setEuclideanDistanceKm();
    }

}
