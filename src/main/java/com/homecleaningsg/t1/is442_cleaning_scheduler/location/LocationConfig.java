package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LocationConfig {

    @Bean
    CommandLineRunner commandLineRunner(LocationRepository repository) {
        return args -> {
            // Disclaimer: These are DEFINITELY random addresses and NOT past residential
            // addresses of the author.
            Location loc1 = new Location("649823", "88 Corporation Road");
            Location loc2 = new Location("438181", "61 Kampong Arang Road");

            repository.saveAll(List.of(loc1, loc2));
        };
    }
}
