package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    public List<Location> getLocations() {
        return List.of(
                new Location("228051", "88 Corporation Road")
        );
    }
}
