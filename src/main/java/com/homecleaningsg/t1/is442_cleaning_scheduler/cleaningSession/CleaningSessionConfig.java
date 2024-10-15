package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class CleaningSessionConfig implements CommandLineRunner {

    private final ContractRepository contractRepository;
    private final CleaningSessionRepository cleaningSessionRepository;

    public CleaningSessionConfig(ContractRepository contractRepository,
                                 CleaningSessionRepository cleaningSessionRepository) {
        this.contractRepository = contractRepository;
        this.cleaningSessionRepository = cleaningSessionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Set contract variable
        Contract contract = contractRepository.findById(1).orElseThrow(() -> new IllegalStateException("Contract not found"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        // Attempt to retrieve the CleaningSession at index 1
        CleaningSession session1 = new CleaningSession(
            contract,
            new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
            new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
            "Session 1",
            CleaningSession.sessionStatus.WORKING
        );
        session1.setSessionRating(CleaningSession.Rating.AVERAGE);
        session1.setSessionFeedback("Feedback 1");

        CleaningSession session2 = new CleaningSession(
                contract,
                new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
                "Session 2",
                CleaningSession.sessionStatus.NOT_STARTED
        );

        cleaningSessionRepository.saveAll(List.of(session1, session2));

        // TODO: Atomize the following code into a separate CommandLineRunner
//        // Create Shift instances and set the Worker and CleaningSession objects
//        Shift ticket1 = new Shift(
//                worker1,
//                session1,
//                new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                Shift.WorkingStatus.WORKING
//        );
//
//        Shift ticket2 = new Shift(
//                worker2,
//                session1,
//                new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                Shift.WorkingStatus.WORKING
//        );
//
//        Shift ticket3 = new Shift(
//                worker1,
//                session2,
//                new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                Shift.WorkingStatus.NOT_STARTED
//        );
//
//        Shift ticket4 = new Shift(
//                worker2,
//                session2,
//                new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                Shift.WorkingStatus.NOT_STARTED
//        );
//
//        shiftRepository.saveAll(List.of(ticket1, ticket2, ticket3, ticket4));
//
//        session1.setShifts(List.of(ticket1, ticket2));
//        session2.setShifts(List.of(ticket3, ticket4));
//
//        // Save the CleaningSession instances to persist the Shifts
//        cleaningSessionRepository.saveAll(List.of(session1, session2));
    }
}