package com.homecleaningsg.t1.is442_cleaning_scheduler.sessionTicket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionTicketConfig {

    @Bean
    CommandLineRunner sessionTicketCommandLineRunner() {
        return args -> {
            // Log temp message
            System.out.println("SessionTicketConfig initialized without creating new SessionTickets.");
        };
    }
}