package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ContractConfig implements CommandLineRunner {

    private final ContractRepository contractRepository;
    private final LocationRepository locationRepository;
    private final ClientRepository clientRepository;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

    public ContractConfig(ContractRepository contractRepository,
                          LocationRepository locationRepository,
                          ClientRepository clientRepository) {
        this.contractRepository = contractRepository;
        this.locationRepository = locationRepository;
        this.clientRepository = clientRepository;

        Location location1 = this.locationRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Location with ID 1 not found"));
        Location location2 = this.locationRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Location with ID 2 not found"));

        Client client1 = this.clientRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Client with ID 1 not found"));
        Client client2 = this.clientRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Client with ID 2 not found"));

        Contract contract1 = new Contract();
        contract1.setContractStart(LocalDate.of(2024,11,3));
        contract1.setContractEnd(LocalDate.of(2024,12,3));
        contract1.setContractComment("Contract 1");
        contract1.setLocation(location1);
        contract1.setPrice(60.0f);
        contract1.setWorkersBudgeted(1);
        contract1.setRooms(1);
        contract1.setFrequency(Contract.Frequency.WEEKLY);
        contract1.setSessionDurationMinutes(60);
        contract1.setClient(client2);
        contract1.setCreationDate(LocalDate.of(2024,11,3));

        Contract contract2 = new Contract();
        contract2.setContractStart(LocalDate.of(2024,9,3));
        contract2.setContractEnd(LocalDate.of(2024,10,3));
        contract2.setContractComment("Contract 2");
        contract2.setLocation(location2);
        contract2.setPrice(250.0f);
        contract2.setWorkersBudgeted(3);
        contract2.setRooms(2);
        contract2.setFrequency(Contract.Frequency.BIWEEKLY);
        contract2.setSessionDurationMinutes(120);
        contract2.setClient(client1);
        contract2.setCreationDate(LocalDate.of(2024,9,1));

        // Log hourly rates
        System.out.println("Contract 1 Rate: " + contract1.getRate());
        System.out.println("Contract 2 Rate: " + contract2.getRate());

        this.contractRepository.saveAll(List.of(contract1, contract2));
    }

    @Override
    public void run(String... args) throws Exception {
    }
}