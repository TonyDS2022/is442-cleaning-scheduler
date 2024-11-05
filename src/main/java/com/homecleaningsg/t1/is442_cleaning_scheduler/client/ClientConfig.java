package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


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
