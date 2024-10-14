package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Configuration
public class CleaningSessionConfig {

    @Bean
    @Order(2) // This bean will run after WorkerConfig
    CommandLineRunner cleaningSessionCommandLineRunner(CleaningSessionRepository cleaningSessionRepository, ContractRepository contractRepository, WorkerRepository workerRepository) {
        return args -> {
            // Set contract variable
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
                contract.setContractStart(new Timestamp(dateFormat.parse("01 Oct 2024").getTime()));
                contract.setContractEnd(new Timestamp(dateFormat.parse("12 Dec 2024").getTime()));
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

            // Fetch Worker instances
            Worker worker1 = workerRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Worker 1 not found"));
            Worker worker2 = workerRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Worker 2 not found"));

            // Attempt to retrieve the CleaningSession at index 1
            CleaningSessionId cleaningSessionId1 = new CleaningSessionId(contract.getContractId(), 1); // Create an instance of CleaningSessionId
            Optional<CleaningSession> cleaningSessionOptional1 = cleaningSessionRepository.findById(cleaningSessionId1);
            CleaningSession session1;
            if (cleaningSessionOptional1.isPresent()) {
                session1 = cleaningSessionOptional1.get();
                System.out.println("CleaningSession found: " + session1);
            } else {
                // Create a new CleaningSession instance if not found
                session1 = new CleaningSession(
                        contract,
                        new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
                        new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
                        "Session 1",
                        CleaningSession.sessionStatus.WORKING
                );
                session1.setSessionRating(CleaningSession.Rating.AVERAGE);
                session1.setSessionFeedback("Feedback 1");
            }

            // Attempt to retrieve the CleaningSession at index 2
            CleaningSessionId cleaningSessionId2 = new CleaningSessionId(contract.getContractId(), 2); // Create an instance of CleaningSessionId
            Optional<CleaningSession> cleaningSessionOptional2 = cleaningSessionRepository.findById(cleaningSessionId2);
            CleaningSession session2;
            if (cleaningSessionOptional2.isPresent()) {
                session2 = cleaningSessionOptional2.get();
                System.out.println("CleaningSession found: " + session2);
            } else {
                // Create a new CleaningSession instance if not found
                session2 = new CleaningSession(
                        contract,
                        new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                        new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                        "Session 2",
                        CleaningSession.sessionStatus.NOT_STARTED
                );
            }

            // Create Shift instances and set the Worker and CleaningSession objects
            Shift ticket1 = new Shift(
                    worker1,
                    session1,
                    new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
                    new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
                    Shift.WorkingStatus.WORKING
            );

            Shift ticket2 = new Shift(
                    worker2,
                    session1,
                    new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
                    new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
                    Shift.WorkingStatus.WORKING
            );

            Shift ticket3 = new Shift(
                    worker1,
                    session2,
                    new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                    new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                    Shift.WorkingStatus.NOT_STARTED
            );

            Shift ticket4 = new Shift(
                    worker2,
                    session2,
                    new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                    new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                    Shift.WorkingStatus.NOT_STARTED
            );

            session1.setShifts(List.of(ticket1, ticket2));
            session2.setShifts(List.of(ticket3, ticket4));

            // Save the CleaningSession instances to persist the Shifts
            cleaningSessionRepository.saveAll(List.of(session1, session2));
        };
    }
}