package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class CleaningSessionService {

    private final CleaningSessionRepository cleaningSessionRepository;

    @Autowired
    public CleaningSessionService(CleaningSessionRepository cleaningSessionRepository) {
        this.cleaningSessionRepository = cleaningSessionRepository;
    }

    public List<CleaningSession> getAllCleaningSessions() {
        return cleaningSessionRepository.findAll();
    }

    public List<CleaningSession> getCleaningSessionsByContractId(Long contractId) {
        return cleaningSessionRepository.findByContract_ContractId(contractId);
    }

    public Optional<CleaningSession> getCleaningSessionByContractIdAndCleaningSessionId(Long contractId, Long cleaningSessionId) {
        return cleaningSessionRepository.findByContract_ContractIdAndCleaningSessionId(contractId, cleaningSessionId);
    }

    public CleaningSession addCleaningSession(CleaningSession cleaningSession){
        Contract contract = cleaningSession.getContract();
        Long durationMinutes = Duration.between(
                cleaningSession.getSessionStartTime(),
                cleaningSession.getSessionEndTime()
        ).toMinutes();


        if (contract.getSessionDurationMinutes() != durationMinutes) {
            throw new IllegalArgumentException("Cleaning session duration must be same as duration set in contract.");
        }

        return cleaningSessionRepository.save(cleaningSession);
    }

    public CleaningSession updateCleaningSession(Long cleaningSessionId, CleaningSession updatedCleaningSession){
        if(!cleaningSessionRepository.existsById(cleaningSessionId)){
            throw new IllegalArgumentException("Cleaning session not found");
        }
        updatedCleaningSession.setCleaningSessionId(cleaningSessionId);
        return cleaningSessionRepository.save(updatedCleaningSession);
    }

    public void deactivateCleaningSession(Long cleaningSessionId) {
        CleaningSession cleaningSession = cleaningSessionRepository.findById(cleaningSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));
        cleaningSession.setActive(false);
        cleaningSessionRepository.save(cleaningSession);
    }


}