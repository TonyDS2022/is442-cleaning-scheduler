package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Configuration
public class CleaningSessionConfig {

    @Bean
    CommandLineRunner cleaningSessionCommandLineRunner(CleaningSessionRepository cleaningSessionRepository, ContractRepository contractRepository) {
        return args -> {
            // set contract variable
            Contract contract = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

            // Fetch a Contract instance with contractId = 1
            Optional<Contract> contractOptional = contractRepository.findById(0);
            if (contractOptional.isPresent()) {
                contract = contractOptional.get();
                System.out.println("Contract found: " + contract);
            } else {
                // Create a Contract instance
                contract = new Contract();
                // contract.setContractStart(new Timestamp(System.currentTimeMillis()));
                contract.setContractStart( new Timestamp(dateFormat.parse("01 Oct 2024").getTime()));
                contract.setContractEnd( new Timestamp(dateFormat.parse("12 Dec 2024").getTime()));
                contract.setContractComment("Sample Comment");
                contract.setOngoing(true);
                contract.setPrice(100.0f);
                contract.setWorkersAssigned(2);
                contract.setRooms(3);
                contract.setFrequency("Weekly");
                contract.setSessionDurationMinutes(60);

                // Save the Contract instance
                contractRepository.save(contract);
            }
            // Create CleaningSession instances and set the Contract object
            CleaningSession session1 = new CleaningSession(
                    contract, // Set the Contract object
                    new Timestamp(dateFormat.parse("05 Oct 2024").getTime()), // sessionStart
                    new Timestamp(dateFormat.parse("05 Oct 2024").getTime()), // sessionEnd
                    "Session 1", // sessionDescription
                    CleaningSession.sessionStatus.WORKING // sessionStatus
            );
            session1.setSessionRating(CleaningSession.Rating.AVERAGE);
            session1.setSessionFeedback("Feedback 1");

            CleaningSession session2 = new CleaningSession(
                    contract, // Set the Contract object
                    new Timestamp(dateFormat.parse("12 Oct 2024").getTime()), // sessionStart
                    new Timestamp(dateFormat.parse("12 Oct 2024").getTime()), // sessionEnd
                    "Session 2", // sessionDescription
                    CleaningSession.sessionStatus.NOT_STARTED // sessionStatus
            );
            // // sessionRating and sessionFeedback are optional
            // session2.setSessionRating(CleaningSession.Rating.GOOD);
            // session2.setSessionFeedback("Feedback 2");

            // Save the CleaningSession instances
            cleaningSessionRepository.saveAll(List.of(session1, session2));

        };
    }
}