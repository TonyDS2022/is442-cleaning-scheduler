package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ContractConfig implements CommandLineRunner {

    private final ContractRepository contractRepository;
    private final LocationRepository locationRepository;
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    public ContractConfig(ContractRepository contractRepository,
                          LocationRepository locationRepository,
                          ClientRepository clientRepository,
                          ClientService clientService) {
        this.contractRepository = contractRepository;
        this.locationRepository = locationRepository;
        this.clientRepository = clientRepository;
        this.clientService = clientService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        initializeContracts();
    }

    public void initializeContracts() {
        Client client1 = this.clientRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Client with ID 1 not found"));
        Client client2 = this.clientRepository.findById(2L)
                .orElseThrow(() -> new IllegalStateException("Client with ID 2 not found"));

        Contract contract1 = new Contract();
        contract1.setContractStart(LocalDate.of(2024, 11, 3));
        contract1.setContractEnd(LocalDate.of(2024, 12, 3));
        contract1.setContractComment("Contract 1");
        contract1.setPrice(60.0f);
        contract1.setWorkersBudgeted(1);
        contract1.setRooms(1);
        contract1.setFrequency(Contract.Frequency.WEEKLY);
        contract1.setSessionDurationMinutes(60);
        contract1.setCreationDate(LocalDate.of(2024, 11, 3));

        Contract contract2 = new Contract();
        contract2.setContractStart(LocalDate.of(2024, 9, 3));
        contract2.setContractEnd(LocalDate.of(2024, 10, 3));
        contract2.setContractComment("Contract 2");
        contract2.setPrice(250.0f);
        contract2.setWorkersBudgeted(3);
        contract2.setRooms(2);
        contract2.setFrequency(Contract.Frequency.BIWEEKLY);
        contract2.setSessionDurationMinutes(120);
        contract2.setCreationDate(LocalDate.of(2024, 9, 1));

        client1.addContract(contract1);
        contract1.setClientSite(client1.getClientSites().get(0));
        client2.addContract(contract2);
        contract2.setClientSite(client2.getClientSites().get(0));

        System.out.println("Contract 1 Rate: " + contract1.getRate());
        System.out.println("Contract 2 Rate: " + contract2.getRate());

        this.clientRepository.saveAll(List.of(client1, client2));
        this.contractRepository.saveAll(List.of(contract1, contract2));
    }
}