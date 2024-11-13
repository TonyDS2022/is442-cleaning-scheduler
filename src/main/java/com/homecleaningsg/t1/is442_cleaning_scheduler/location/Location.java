package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.Subzone;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.SubzoneRepository;
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

    private Double latitude;
    private Double longitude;

    //@NonNull
    @ManyToOne
    @JoinColumn(name = "subzoneId", referencedColumnName = "subzoneId")
    private Subzone subzone;

    public void setSubzone(SubzoneRepository subzoneRepository) {
        //check that latitude and longitude are not null
        if (latitude != null && longitude != null) {
            //get the subzone from the latitude and longitude
            subzone = subzoneRepository.findSubzoneByLatLong(latitude, longitude);
        }
    }

    public double getEuclideanDistanceKmFrom(Location another) {
        double EARTH_RADIUS_KM = 6371.0;
        //check that latitude and longitude are not null
        if (latitude != null && longitude != null && another.latitude != null && another.longitude != null) {
            // convert latitude and longitude to radians
            double lat1 = Math.toRadians(latitude);
            double lon1 = Math.toRadians(longitude);
            double lat2 = Math.toRadians(another.latitude);
            double lon2 = Math.toRadians(another.longitude);

            // convert lat/lon to Cartesian coordinates
            double x1 = EARTH_RADIUS_KM * Math.cos(lat1) * Math.cos(lon1);
            double y1 = EARTH_RADIUS_KM * Math.cos(lat1) * Math.sin(lon1);
            double x2 = EARTH_RADIUS_KM * Math.cos(lat2) * Math.cos(lon2);
            double y2 = EARTH_RADIUS_KM * Math.cos(lat2) * Math.sin(lon2);

            // Calculate the Euclidean distance
            return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        }
        return -1;
    }

    public boolean equals(Object other) {
        return other instanceof Location && ((Location) other).getPostalCode().equals(this.getPostalCode());
    }
}
