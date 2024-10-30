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
        Location loc1 = new Location("88 Corporation Road", "649823");
        Location loc2 = new Location("61 Kampong Arang Road", "438181");
        Location loc3 = new Location("20 Orchard Road", "238830");

        this.locationRepository.saveAll(List.of(loc1, loc2, loc3));

        this.locationService.updateLocationLatLong().subscribe();
    }

    @Override
    public void run(String... args) throws Exception {

    }
}