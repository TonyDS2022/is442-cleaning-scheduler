package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.Subzone;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table
public class Location {
    @Id
    @SequenceGenerator(
            name = "location_sequence",
            sequenceName = "location_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "location_sequence"
    )
    @JsonIgnore
    private Long locationId;

    @NonNull
    private String address;
    @NonNull
    private String postalCode;

    @JsonIgnore
    private Double latitude;
    @JsonIgnore
    private Double Longitude;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "subzoneId", referencedColumnName = "subzoneId")
    private Subzone subzone;
}
