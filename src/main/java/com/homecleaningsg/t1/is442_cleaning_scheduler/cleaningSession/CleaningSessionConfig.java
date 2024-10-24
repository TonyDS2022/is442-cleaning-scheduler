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
            LocalTime.of(8,0),
            LocalDate.of(2024,10,5),
            LocalTime.of(12,0),
            "Session 1",
            CleaningSession.sessionStatus.WORKING
        );
        session1.setSessionRating(CleaningSession.Rating.AVERAGE);
        session1.setSessionFeedback("Feedback 1");
        session1.setPlanningStage(CleaningSession.PlanningStage.GREEN);

        // Ensure the location is set correctly
        session1.setLocation(contract.getLocation());

        CleaningSession session2 = new CleaningSession(
            contract,
            LocalDate.of(2024,10,12),
            LocalTime.of(14,0),
            LocalDate.of(2024,10,12),
            LocalTime.of(17,0),
            "Session 2",
            CleaningSession.sessionStatus.NOT_STARTED
        );

        this.cleaningSessionRepository.saveAll(List.of(session1, session2));
    }

    @Override
    public void run(String... args) throws Exception {
    }
}