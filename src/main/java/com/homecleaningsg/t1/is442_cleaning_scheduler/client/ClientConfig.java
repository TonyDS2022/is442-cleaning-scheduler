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
    private final ContractRepository contractRepository;
    private final LocationRepository locationRepository;


    public ClientConfig(ClientRepository clientRepository,
                        ContractRepository contractRepository,
                        LocationRepository locationRepository) {
        this.clientRepository = clientRepository;
        this.contractRepository = contractRepository;
        this.locationRepository = locationRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        Location location1 = locationRepository.findById(1L).orElse(null);
        Location location2 = locationRepository.findById(2L).orElse(null);
        Contract contract1 = contractRepository.findById(1L).orElse(null);
        Contract contract2 = contractRepository.findById(2L).orElse(null);
        Client ad1 = new Client("Amy Santiago", "98472094", true, location1, contract1);
        Client ad2 = new Client("Jake Peralta", "92384923", true, location2, contract2);
        clientRepository.saveAll(List.of(ad1, ad2));
    }
}
