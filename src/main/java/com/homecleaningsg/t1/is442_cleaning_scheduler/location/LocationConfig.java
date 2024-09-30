package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class LocationConfig {

    @Bean
    @Order(2)
    CommandLineRunner locationCommandLineRunner(
            LocationRepository locationRepository,
            LocationService locationService
    ) {
        return args -> {
            // Disclaimer: These are DEFINITELY random addresses and NOT past residential
            // addresses of the author.
            //Subzone subzone1 = subzoneRepository.findSubzoneBySubzoneName("TAMAN JURONG");
            //Subzone subzone2 = subzoneRepository.findSubzoneBySubzoneName("TANJONG RHU");
            Location loc1 = new Location("649823", "88 Corporation Road");
            Location loc2 = new Location("438181", "61 Kampong Arang Road");

            locationRepository.saveAll(List.of(loc1, loc2));

            // update location coordinates
            locationService.updateLocationLatLong().subscribe();
        };
    }
}
