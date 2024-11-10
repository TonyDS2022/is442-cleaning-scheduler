package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

        Location location1 = this.locationRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Location with ID 1 not found"));
        Location location2 = this.locationRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Location with ID 2 not found"));

        Client client1 = new Client("Amy Santiago", "98472094", true, location1, LocalDate.of(2024,10,4));

        Client client2 = new Client("Jake Peralta", "92384923", true, location2, LocalDate.of(2024,8,2));
        client1.setDeactivatedAt(LocalDate.of(2024, 11, 4));
        client1.setActive(false);

        this.clientRepository.saveAll(List.of(client1, client2));
    }
    @Override
    public void run(String... args) throws Exception {
    }
}
