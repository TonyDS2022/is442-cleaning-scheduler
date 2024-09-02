package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class Location {
    @JsonIgnore Long id;
    @NonNull String address;
    @NonNull String postalCode;
    @JsonIgnore Double latitude;
    @JsonIgnore Double Longitude;
    @JsonIgnore Long regionId;
    String regionName;
    @JsonIgnore Long planningAreaId;
    String planningAreaName;
    @JsonIgnore Long subzoneId;
    String subzoneName;
}
