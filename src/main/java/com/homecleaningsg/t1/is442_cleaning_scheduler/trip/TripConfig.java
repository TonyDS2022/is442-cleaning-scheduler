package com.homecleaningsg.t1.is442_cleaning_scheduler.trip;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class TripConfig {

    @Bean
    @Order(3)
    CommandLineRunner tripCommandLineRunner(TripService tripService) {
        return args -> {
            tripService.buildTrips();
            tripService.updateTripDistanceDurationAsync().subscribe();
        };
    }
}
