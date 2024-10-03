package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.List;

@Configuration
public class CleaningSessionConfig {

    @Bean
    CommandLineRunner cleaningSessionCommandLineRunner(CleaningSessionRepository cleaningSessionRepository, ContractRepository contractRepository) {
        return args -> {
            // Create a Contract instance
            Contract contract = new Contract();
            contract.setContractStart(new Timestamp(System.currentTimeMillis()));
            contract.setContractEnd(new Timestamp(System.currentTimeMillis()));
            contract.setContractComment("Sample Comment");
            contract.setOngoing(true);
            contract.setPrice(100.0f);
            contract.setWorkersAssigned(2);
            contract.setRooms(3);
            contract.setFrequency("Weekly");
            contract.setSessionDuration(60);

            // Save the Contract instance
            contractRepository.save(contract);

            // Create CleaningSession instances and set the Contract object
            CleaningSession session1 = new CleaningSession(
                    contract, // Set the Contract object
                    new Timestamp(System.currentTimeMillis()), // sessionStart
                    new Timestamp(System.currentTimeMillis()), // sessionEnd
                    "Session 1", // sessionDescription
                    CleaningSession.sessionStatus.NOT_STARTED // sessionStatus
            );
            session1.setSessionRating(CleaningSession.Rating.AVERAGE);
            session1.setSessionFeedback("Feedback 1");

            CleaningSession session2 = new CleaningSession(
                    contract, // Set the Contract object
                    new Timestamp(System.currentTimeMillis()), // sessionStart
                    new Timestamp(System.currentTimeMillis()), // sessionEnd
                    "Session 2", // sessionDescription
                    CleaningSession.sessionStatus.WORKING // sessionStatus
            );
            session2.setSessionRating(CleaningSession.Rating.GOOD);
            session2.setSessionFeedback("Feedback 2");

            // Save the CleaningSession instances
            cleaningSessionRepository.saveAll(List.of(session1, session2));
        };
    }
}