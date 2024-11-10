package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class CleaningSessionConfig implements CommandLineRunner {

    private final ContractRepository contractRepository;
    private final CleaningSessionRepository cleaningSessionRepository;

    public CleaningSessionConfig(ContractRepository contractRepository,
                                 CleaningSessionRepository cleaningSessionRepository) {
        this.contractRepository = contractRepository;
        this.cleaningSessionRepository = cleaningSessionRepository;

        Contract contract = this.contractRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Contract not found"));

        // Attempt to retrieve the CleaningSession at index 1
        CleaningSession session1 = new CleaningSession(
            contract,
            LocalDate.of(2024,10,5),
            LocalTime.of(9,0),
            LocalDate.of(2024,10,5),
            LocalTime.of(12,0),
            "Session 1"
        );
        session1.setSessionRating(CleaningSession.Rating.AVERAGE);
        session1.setSessionFeedback("Feedback 1");

        CleaningSession session2 = new CleaningSession(
            contract,
            LocalDate.of(2024,10,12),
            LocalTime.of(14,0),
            LocalDate.of(2024,10,12),
            LocalTime.of(17,0),
            "Session 2"
        );
        session2.setSessionRating(CleaningSession.Rating.GOOD);
        session2.setSessionFeedback("Feedback 2");

        CleaningSession session3 = new CleaningSession(
                contract,
                LocalDate.of(2024,11,3),
                LocalTime.of(9,0),
                LocalDate.of(2024,11,3),
                LocalTime.of(12,0),
                "Session 3"
        );
        session3.setSessionStatus(CleaningSession.SessionStatus.CANCELLED);
        session3.setCancelledAt(LocalDate.of(2024,11,1));

        this.cleaningSessionRepository.saveAll(List.of(session1, session2, session3));
    }

    @Override
    public void run(String... args) throws Exception {
    }
}