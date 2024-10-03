package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.List;

@Configuration
public class ContractConfig {

    @Bean
    CommandLineRunner contractCommandLineRunner(ContractRepository repository) {
        return args -> {
            Contract contract1 = new Contract();
            contract1.setContractStart(new Timestamp(System.currentTimeMillis()));
            contract1.setContractEnd(new Timestamp(System.currentTimeMillis()));
            contract1.setContractComment("Contract 1");
            contract1.setOngoing(true);
            contract1.setPrice(100.0f);
            contract1.setWorkersAssigned(2);
            contract1.setRooms(1);
            contract1.setFrequency("Weekly");
            contract1.setSessionDuration(60);

            Contract contract2 = new Contract();
            contract2.setContractStart(new Timestamp(System.currentTimeMillis()));
            contract2.setContractEnd(new Timestamp(System.currentTimeMillis()));
            contract2.setContractComment("Contract 2");
            contract2.setOngoing(false);
            contract2.setPrice(200.0f);
            contract2.setWorkersAssigned(3);
            contract2.setRooms(2);
            contract2.setFrequency("Bi-weekly");
            contract2.setSessionDuration(120);

            // Log hourly rates
            System.out.println("Contract 1 Hourly Rate: " + contract1.getHourlyRate());
            System.out.println("Contract 2 Hourly Rate: " + contract2.getHourlyRate());

            repository.saveAll(List.of(contract1, contract2));
        };
    }
}