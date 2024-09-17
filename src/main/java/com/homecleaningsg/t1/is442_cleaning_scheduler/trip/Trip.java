package com.homecleaningsg.t1.is442_cleaning_scheduler.trip;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;

@NoArgsConstructor
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

    @NonNull
    private double tripDuration;
}
