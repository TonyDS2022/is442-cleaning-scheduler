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
    CommandLineRunner tripCommandLineRunner(TripRepository tripRepository, LocationRepository locationRepository) {
        return args -> {
            Location origin = locationRepository.findById(1L) //
                .orElseThrow(() -> new IllegalArgumentException("Origin location not found"));
            Location destination = locationRepository.findById(2L) //
                .orElseThrow(() -> new IllegalArgumentException("Destination location not found"));
            Trip trip = new Trip(origin, destination, 30.5);

            tripRepository.save(trip);
        };
    }
}
