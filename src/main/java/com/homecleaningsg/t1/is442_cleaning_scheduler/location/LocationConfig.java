package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationConfig implements CommandLineRunner {

    private final LocationRepository locationRepository;
    private final LocationService locationService;

    public LocationConfig(LocationRepository locationRepository, LocationService locationService) {
        this.locationRepository = locationRepository;
        this.locationService = locationService;
    }

    @Override
    public void run(String... args) throws Exception {
        Location loc1 = new Location("649823", "88 Corporation Road");
        Location loc2 = new Location("438181", "61 Kampong Arang Road");

        locationRepository.saveAll(List.of(loc1, loc2));

        locationService.updateLocationLatLong().subscribe();
    }
}