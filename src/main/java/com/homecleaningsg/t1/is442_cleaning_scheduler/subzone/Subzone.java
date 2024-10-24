package com.homecleaningsg.t1.is442_cleaning_scheduler.subzone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Geometry;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table
public class Subzone {
    @Id
    @SequenceGenerator(
            name = "subzone_sequence",
            sequenceName = "subzone_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subzone_sequence"
    )
    @JsonIgnore
    private Long subzoneId;

    @NonNull
    private String subzoneName;

    @NonNull
    private String planningAreaName;

    @NonNull
    private String regionName;

    @NonNull
    @JsonIgnore
    @Column(columnDefinition = "geometry")
    private Geometry subzoneGeometry;
}
