package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiftConfig {

    @Bean
    CommandLineRunner shiftCommandLineRunner() {
        return args -> {
            // Log temp message
            System.out.println("ShiftConfig initialized without creating new shifts.");
        };
    }
}