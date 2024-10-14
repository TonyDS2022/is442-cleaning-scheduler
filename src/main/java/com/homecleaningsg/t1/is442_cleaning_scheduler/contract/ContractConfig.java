package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Order(1) // This is to ensure that this CommandLineRunner runs first
public class ContractConfig implements CommandLineRunner {

    private final ContractRepository contractRepository;
    private final LocationRepository locationRepository;

    public ContractConfig(ContractRepository contractRepository, LocationRepository locationRepository) {
        this.contractRepository = contractRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Location loc1 = locationRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Location 1 not found"));
        Location loc2 = locationRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Location 2 not found"));

        Contract contract1 = new Contract();
        contract1.setContractStart(new Timestamp(dateFormat.parse("01 Oct 2024").getTime()));
        contract1.setContractEnd(new Timestamp(dateFormat.parse("01 Jan 2025").getTime()));
        contract1.setContractComment("Contract 1");
        contract1.setLocation(loc1);
        contract1.setOngoing(true);
        contract1.setPrice(60.0f);
        contract1.setWorkersBudgeted(1);
        contract1.setRooms(1);
        contract1.setFrequency("Weekly");
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
        contract2.setFrequency("Bi-weekly");
        contract2.setSessionDurationMinutes(120);

        // Log hourly rates
        System.out.println("Contract 1 Rate: " + contract1.getRate());
        System.out.println("Contract 2 Rate: " + contract2.getRate());

        contractRepository.saveAll(List.of(contract1, contract2));
    }
}