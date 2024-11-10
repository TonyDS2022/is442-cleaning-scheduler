package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ContractConfig implements CommandLineRunner {

    private final ContractRepository contractRepository;
    private final LocationRepository locationRepository;
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

    public ContractConfig(ContractRepository contractRepository,
                          LocationRepository locationRepository,
                          ClientRepository clientRepository,
                          ClientService clientService) {
        this.contractRepository = contractRepository;
        this.locationRepository = locationRepository;
        this.clientRepository = clientRepository;
        this.clientService = clientService;

        Location location1 = this.locationRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Location with ID 1 not found"));
        Location location2 = this.locationRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Location with ID 2 not found"));

        Client client1 = new Client("Amy Santiago", "98472094", true);
        Client client2 = new Client("Jake Peralta", "92384923", true);

        ClientSite clientSite1 = new ClientSite(client1, location1.getAddress(), location1.getPostalCode(), "#01-01", location1);
        ClientSite clientSite2 = new ClientSite(client2, location2.getAddress(), location2.getPostalCode(), "#02-02", location2);

        Contract contract1 = new Contract();
        contract1.setContractStart(Timestamp.valueOf(LocalDateTime.parse("01 Oct 2024 00:00:00", this.dateTimeFormatter)));
        contract1.setContractEnd(Timestamp.valueOf(LocalDateTime.parse("01 Jan 2025 00:00:00", this.dateTimeFormatter)));
        contract1.setContractComment("Contract 1");
        contract1.setClientSite(clientSite1);
        contract1.setPrice(60.0f);
        contract1.setWorkersBudgeted(1);
        contract1.setRooms(1);
        contract1.setFrequency(Contract.Frequency.WEEKLY);
        contract1.setSessionDurationMinutes(60);

        Contract contract2 = new Contract();
        contract2.setContractStart(new Timestamp(System.currentTimeMillis()));
        contract2.setContractEnd(new Timestamp(System.currentTimeMillis()));
        contract2.setContractComment("Contract 2");
        contract2.setClientSite(clientSite2);
        contract2.setPrice(250.0f);
        contract2.setWorkersBudgeted(3);
        contract2.setRooms(2);
        contract2.setFrequency(Contract.Frequency.BIWEEKLY);
        contract2.setSessionDurationMinutes(120);

        client1.addContract(contract1);
        client2.addContract(contract2);

        // Log hourly rates
        System.out.println("Contract 1 Rate: " + contract1.getRate());
        System.out.println("Contract 2 Rate: " + contract2.getRate());

        this.clientRepository.saveAll(List.of(client1, client2));
        this.contractRepository.saveAll(List.of(contract1, contract2));
    }

    @Override
    public void run(String... args) throws Exception {
    }
}