package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CleaningSessionConfig implements CommandLineRunner {

    private final ContractRepository contractRepository;
    private final CleaningSessionRepository cleaningSessionRepository;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

    public CleaningSessionConfig(ContractRepository contractRepository,
                                 CleaningSessionRepository cleaningSessionRepository) {
        this.contractRepository = contractRepository;
        this.cleaningSessionRepository = cleaningSessionRepository;

        Contract contract = this.contractRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Contract not found"));

        // Attempt to retrieve the CleaningSession at index 1
        CleaningSession session1 = new CleaningSession(
            contract,
            Timestamp.valueOf(LocalDateTime.parse("05 Oct 2024 00:00:00", this.dateTimeFormatter)),
            Timestamp.valueOf(LocalDateTime.parse("05 Oct 2024 00:00:00", this.dateTimeFormatter)),
            "Session 1",
            CleaningSession.sessionStatus.WORKING
        );
        session1.setSessionRating(CleaningSession.Rating.AVERAGE);
        session1.setSessionFeedback("Feedback 1");

        CleaningSession session2 = new CleaningSession(
            contract,
            Timestamp.valueOf(LocalDateTime.parse("12 Oct 2024 00:00:00", this.dateTimeFormatter)),
            Timestamp.valueOf(LocalDateTime.parse("12 Oct 2024 00:00:00", this.dateTimeFormatter)),
            "Session 2",
            CleaningSession.sessionStatus.NOT_STARTED
        );

        this.cleaningSessionRepository.saveAll(List.of(session1, session2));
    }

    @Override
    public void run(String... args) throws Exception {
    }
}