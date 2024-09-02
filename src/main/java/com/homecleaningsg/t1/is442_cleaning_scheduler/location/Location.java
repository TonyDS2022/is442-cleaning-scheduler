package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
            generator = "student_sequence"
    )
    @JsonIgnore
    private Long id;

    @NonNull
    private String address;
    @NonNull
    private String postalCode;

    @JsonIgnore
    private Double latitude;
    @JsonIgnore
    private Double Longitude;

    @JsonIgnore
    private Long regionId;
    private String regionName;

    @JsonIgnore
    private Long planningAreaId;
    private String planningAreaName;

    @JsonIgnore
    private Long subzoneId;
    private String subzoneName;
}
