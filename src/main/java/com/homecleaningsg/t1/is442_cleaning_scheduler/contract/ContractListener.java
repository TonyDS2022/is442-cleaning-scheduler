package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.persistence.PostPersist;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ContractListener {

    private final CleaningSessionRepository cleaningSessionRepository;
    private final ShiftRepository shiftRepository;

    @Autowired
    public ContractListener(@Lazy CleaningSessionRepository cleaningSessionRepository, @Lazy ShiftRepository shiftRepository) {
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.shiftRepository = shiftRepository;
    }

    // Create CleaningSession and Shift when a new Contract is created
    @PostPersist
    public void onPostPersist(Contract contract) {
        System.out.println("Contract persisted: " + contract.getContractId());

        // Create CleaningSessions based on Contract on contractStart
        CleaningSession cleaningSession = new CleaningSession(
                contract,
                contract.getContractStart().toLocalDateTime().toLocalDate(),
                contract.getContractStart().toLocalDateTime().toLocalTime(),
                contract.getContractEnd().toLocalDateTime().toLocalDate(),
                contract.getContractEnd().toLocalDateTime().toLocalTime(),
                "Session 1",
                CleaningSession.sessionStatus.NOT_STARTED
        );
        cleaningSession.setLocation(contract.getLocation());
        cleaningSession.setPlanningStage(CleaningSession.PlanningStage.RED);
        cleaningSessionRepository.save(cleaningSession);
    }

    // // Create CleaningSessions based on Contract frequency between contractStart to contractEnd
    // ConcurrentLinkedQueue<CleaningSession> sessionsToSave = new ConcurrentLinkedQueue<>();
    // Timestamp sessionStart = contract.getContractStart();
    // Timestamp sessionEnd = contract.getContractEnd();
    // while (sessionStart.before(sessionEnd)) {
    //     CleaningSession newCleaningSession = new CleaningSession(
    //             contract,
    //             sessionStart.toLocalDateTime().toLocalDate(),
    //             LocalTime.of(8, 0),
    //             sessionEnd.toLocalDateTime().toLocalDate(),
    //             LocalTime.of(12, 0),
    //             "Session 1",
    //             CleaningSession.sessionStatus.NOT_STARTED
    //     );
    //     newCleaningSession.setLocation(contract.getLocation());
    //     sessionsToSave.add(newCleaningSession);
    //
    //     // // Create Shift
    //     // Shift shift = new Shift();
    //     // shift.setCleaningSession(cleaningSession);
    //     // shift.setShiftStart(sessionStart);
    //     // shift.setShiftEnd(sessionEnd);
    //     // shiftRepository.save(shift);
    //
    //     // Increment sessionStart based on Contract frequency
    //     sessionStart = switch (contract.getFrequency()) {
    //         case DAILY -> Timestamp.valueOf(sessionStart.toLocalDateTime().plusDays(1));
    //         case WEEKLY -> Timestamp.valueOf(sessionStart.toLocalDateTime().plusWeeks(1));
    //         case BIWEEKLY -> Timestamp.valueOf(sessionStart.toLocalDateTime().plusWeeks(2));
    //         case MONTHLY -> Timestamp.valueOf(sessionStart.toLocalDateTime().plusMonths(1));
    //         default -> sessionStart;
    //     };
    // }
    // // // attempt to save all sessions at once in thread save manner to avoid java.util.ConcurrentModificationException
    // cleaningSessionRepository.saveAll(sessionsToSave);
    // // synchronized (this) {
    // //     cleaningSessionRepository.saveAll(sessionsToSave);
    // // }
}
    // // Update CleaningSession and Shift when a Contract is updated
    // @PostUpdate
    // public void onPostUpdate(Contract contract) {
    //     // Update all CleaningSession associated with the contract
    //     List<CleaningSession> getCleaningSessionsByContractId(contract.getContractId()).forEach(cleaningSession -> {
    //         cleaningSession.setSessionStartDate(contract.getContractStart());
    //         cleaningSession.setSessionEndDate(contract.getContractEnd());
    //         cleaningSessionRepository.save(cleaningSession);
    //     });
    //
    //
    //     // // Update Shift
    //     // Shift shift = shiftRepository.findByCleaningSession(cleaningSession);
    //     // shift.setShiftStart(contract.getContractStart());
    //     // shift.setShiftEnd(contract.getContractEnd());
    //     // shiftRepository.save(shift);
    // }
