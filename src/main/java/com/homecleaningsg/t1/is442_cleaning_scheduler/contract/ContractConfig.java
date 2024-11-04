package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

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
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

    public ContractConfig(ContractRepository contractRepository, LocationRepository locationRepository) {
        this.contractRepository = contractRepository;
        this.locationRepository = locationRepository;

        Location loc1 = this.locationRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Location 1 not found"));
        Location loc2 = this.locationRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Location 2 not found"));

        Contract contract1 = new Contract();
        contract1.setContractStart(Timestamp.valueOf(LocalDateTime.parse("01 Oct 2024 00:00:00", this.dateTimeFormatter)));
        contract1.setContractEnd(Timestamp.valueOf(LocalDateTime.parse("01 Jan 2025 00:00:00", this.dateTimeFormatter)));
        contract1.setContractComment("Contract 1");
        contract1.setLocation(loc1);
        contract1.setOngoing(true);
        contract1.setPrice(60.0f);
        contract1.setWorkersBudgeted(1);
        contract1.setRooms(1);
        contract1.setFrequency(Contract.Frequency.WEEKLY);
        contract1.setSessionDurationMinutes(60);

        Contract contract2 = new Contract();
        contract2.setContractStart(new Timestamp(System.currentTimeMillis()));
        contract2.setContractEnd(new Timestamp(System.currentTimeMillis()));
        contract2.setContractComment("Contract 2");
        contract2.setLocation(loc2);
        contract2.setOngoing(false);
        contract2.setPrice(250.0f);
        contract2.setWorkersBudgeted(3);
        contract2.setRooms(2);
        contract2.setFrequency(Contract.Frequency.BIWEEKLY);
        contract2.setSessionDurationMinutes(120);

        // Log hourly rates
        System.out.println("Contract 1 Rate: " + contract1.getRate());
        System.out.println("Contract 2 Rate: " + contract2.getRate());

        this.contractRepository.saveAll(List.of(contract1, contract2));
    }

    @Override
    public void run(String... args) throws Exception {
    }
}