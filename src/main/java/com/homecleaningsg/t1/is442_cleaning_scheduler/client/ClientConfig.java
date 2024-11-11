package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class ClientConfig implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final LocationRepository locationRepository;


    public ClientConfig(ClientRepository clientRepository,
                        LocationRepository locationRepository) {
        this.clientRepository = clientRepository;
        this.locationRepository = locationRepository;
    }
    @Override
    public void run(String... args) throws Exception {

    }
}
