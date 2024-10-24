package com.homecleaningsg.t1.is442_cleaning_scheduler.trip;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TripConfig implements CommandLineRunner {

    private final TripService tripService;

    public TripConfig(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public void run(String... args) throws Exception {
        tripService.buildTrips();
        tripService.updateTripDistanceDurationAsync().subscribe();
    }
}
