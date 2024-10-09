// package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;
//
// import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
// import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
// import com.homecleaningsg.t1.is442_cleaning_scheduler.sessionTicket.SessionTicket;
// import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
// import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// import java.sql.Timestamp;
// import java.text.SimpleDateFormat;
// import java.util.List;
// import java.util.Optional;
//
// @Configuration
// public class CleaningSessionConfig {
//
//     @Bean
//     CommandLineRunner cleaningSessionCommandLineRunner(CleaningSessionRepository cleaningSessionRepository, ContractRepository contractRepository, WorkerRepository workerRepository) {
//         return args -> {
//             // set contract variable
//             Contract contract = null;
//             SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
//
//             // Fetch a Contract instance with contractId = 1
//             Optional<Contract> contractOptional = contractRepository.findById(0);
//             if (contractOptional.isPresent()) {
//                 contract = contractOptional.get();
//                 System.out.println("Contract found: " + contract);
//             } else {
//                 // Create a Contract instance
//                 contract = new Contract();
//                 contract.setContractStart(new Timestamp(dateFormat.parse("01 Oct 2024").getTime()));
//                 contract.setContractEnd(new Timestamp(dateFormat.parse("12 Dec 2024").getTime()));
//                 contract.setContractComment("Sample Comment");
//                 contract.setOngoing(true);
//                 contract.setPrice(100.0f);
//                 contract.setWorkersAssigned(2);
//                 contract.setRooms(3);
//                 contract.setFrequency("Weekly");
//                 contract.setSessionDurationMinutes(60);
//
//                 // Save the Contract instance
//                 contractRepository.save(contract);
//             }
//
//             // Fetch or create a Worker instance
//             Worker worker = workerRepository.findById(1L).orElse(null);
//             if (worker == null) {
//                 worker = new Worker(
//                         "John Doe",
//                         "john",
//                         "john1243",
//                         "john@gmail.com",
//                         "99999999",
//                         "Cleaner with 20 years experience in cleaning bungalows/landed properties",
//                         "1pm-10pm"
//                 );
//                 workerRepository.save(worker);
//             }
//
//             // Create CleaningSession instances and set the Contract object
//             CleaningSession session1 = new CleaningSession(
//                     contract,
//                     new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                     new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                     "Session 1",
//                     CleaningSession.sessionStatus.WORKING
//             );
//             session1.setSessionRating(CleaningSession.Rating.AVERAGE);
//             session1.setSessionFeedback("Feedback 1");
//
//             CleaningSession session2 = new CleaningSession(
//                     contract,
//                     new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                     new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                     "Session 2",
//                     CleaningSession.sessionStatus.NOT_STARTED
//             );
//
//             // // Save the CleaningSession instances
//             // cleaningSessionRepository.saveAll(List.of(session1, session2));
//
//             // Create SessionTicket instances and set the Worker and CleaningSession objects
//             SessionTicket ticket1 = new SessionTicket(
//                     worker,
//                     session1,
//                     new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                     new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                     SessionTicket.WorkingStatus.WORKING
//             );
//
//             SessionTicket ticket2 = new SessionTicket(
//                     worker,
//                     session2,
//                     new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                     new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                     SessionTicket.WorkingStatus.NOT_STARTED
//             );
//
//             session1.setSessionTickets(List.of(ticket1));
//             session2.setSessionTickets(List.of(ticket2));
//
//             // Save the CleaningSession instances again to persist the SessionTickets
//             cleaningSessionRepository.saveAll(List.of(session1, session2));
//         };
//     }
// }

// package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;
//
// import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
// import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
// import com.homecleaningsg.t1.is442_cleaning_scheduler.sessionTicket.SessionTicket;
// import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
// import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// import java.sql.Timestamp;
// import java.text.SimpleDateFormat;
// import java.util.List;
// import java.util.Optional;
//
// @Configuration
// public class CleaningSessionConfig {
//
//     @Bean
//     CommandLineRunner cleaningSessionCommandLineRunner(CleaningSessionRepository cleaningSessionRepository, ContractRepository contractRepository, WorkerRepository workerRepository) {
//         return args -> {
//             // Set contract variable
//             Contract contract = null;
//             SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
//
//             // Fetch a Contract instance with contractId = 1
//             Optional<Contract> contractOptional = contractRepository.findById(0);
//             if (contractOptional.isPresent()) {
//                 contract = contractOptional.get();
//                 System.out.println("Contract found: " + contract);
//             } else {
//                 // Create a Contract instance
//                 contract = new Contract();
//                 contract.setContractStart(new Timestamp(dateFormat.parse("01 Oct 2024").getTime()));
//                 contract.setContractEnd(new Timestamp(dateFormat.parse("12 Dec 2024").getTime()));
//                 contract.setContractComment("Sample Comment");
//                 contract.setOngoing(true);
//                 contract.setPrice(100.0f);
//                 contract.setWorkersAssigned(2);
//                 contract.setRooms(3);
//                 contract.setFrequency("Weekly");
//                 contract.setSessionDurationMinutes(60);
//
//                 // Save the Contract instance
//                 contractRepository.save(contract);
//             }
//
//             // Fetch or create a Worker instance
//             Worker worker = workerRepository.findById(1L).orElse(null);
//             if (worker == null) {
//                 worker = new Worker(
//                         "John Doe",
//                         "john",
//                         "john1243",
//                         "john@gmail.com",
//                         "99999999",
//                         "Cleaner with 20 years experience in cleaning bungalows/landed properties",
//                         "1pm-10pm"
//                 );
//                 workerRepository.save(worker);
//             }
//
//             // Attempt to retrieve the CleaningSession at index 1
//             CleaningSessionId cleaningSessionId = new CleaningSessionId(contract.getContractId(), 1); // Create an instance of CleaningSessionId
//             Optional<CleaningSession> cleaningSessionOptional = cleaningSessionRepository.findById(cleaningSessionId);
//             CleaningSession session1;
//             if (cleaningSessionOptional.isPresent()) {
//                 session1 = cleaningSessionOptional.get();
//                 System.out.println("CleaningSession found: " + session1);
//             } else {
//                 // Create a new CleaningSession instance if not found
//                 session1 = new CleaningSession(
//                         contract,
//                         new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                         new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                         "Session 1",
//                         CleaningSession.sessionStatus.WORKING
//                 );
//                 session1.setSessionRating(CleaningSession.Rating.AVERAGE);
//                 session1.setSessionFeedback("Feedback 1");
//             }
//
//             // Create another CleaningSession instance
//             CleaningSession session2 = new CleaningSession(
//                     contract,
//                     new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                     new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                     "Session 2",
//                     CleaningSession.sessionStatus.NOT_STARTED
//             );
//
//             // Create SessionTicket instances and set the Worker and CleaningSession objects
//             SessionTicket ticket1 = new SessionTicket(
//                     worker,
//                     session1,
//                     new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                     new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                     SessionTicket.WorkingStatus.WORKING
//             );
//
//             SessionTicket ticket2 = new SessionTicket(
//                     worker,
//                     session2,
//                     new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                     new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                     SessionTicket.WorkingStatus.NOT_STARTED
//             );
//
//             session1.setSessionTickets(List.of(ticket1));
//             session2.setSessionTickets(List.of(ticket2));
//
//             // Save the CleaningSession instances to persist the SessionTickets
//             cleaningSessionRepository.saveAll(List.of(session1, session2));
//         };
//     }
// }

package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.sessionTicket.SessionTicket;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Component
public class CleaningSessionConfig implements CommandLineRunner {

    private final CleaningSessionRepository cleaningSessionRepository;
    private final ContractRepository contractRepository;
    private final WorkerRepository workerRepository;

    public CleaningSessionConfig(CleaningSessionRepository cleaningSessionRepository, ContractRepository contractRepository, WorkerRepository workerRepository) {
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.contractRepository = contractRepository;
        this.workerRepository = workerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Set contract variable
        Contract contract;
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

        // Create SessionTicket instances and set the Worker and CleaningSession objects
        SessionTicket ticket1 = new SessionTicket(
                worker1,
                session1,
                new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
                new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
                SessionTicket.WorkingStatus.WORKING
        );

        SessionTicket ticket2 = new SessionTicket(
                worker2,
                session1,
                new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
                new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
                SessionTicket.WorkingStatus.WORKING
        );

        SessionTicket ticket3 = new SessionTicket(
                worker1,
                session2,
                new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                SessionTicket.WorkingStatus.NOT_STARTED
        );

        SessionTicket ticket4 = new SessionTicket(
                worker2,
                session2,
                new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                SessionTicket.WorkingStatus.NOT_STARTED
        );

        session1.setSessionTickets(List.of(ticket1, ticket2));
        session2.setSessionTickets(List.of(ticket3, ticket4));

        // Save the CleaningSession instances to persist the SessionTickets
        cleaningSessionRepository.saveAll(List.of(session1, session2));
    }
}