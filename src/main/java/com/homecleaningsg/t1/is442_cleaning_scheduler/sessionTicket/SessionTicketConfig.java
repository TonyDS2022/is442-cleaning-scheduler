package com.homecleaningsg.t1.is442_cleaning_scheduler.sessionTicket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class SessionTicketConfig implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Log temp message
        System.out.println("SessionTicketConfig initialized without creating new SessionTickets.");
    }
}