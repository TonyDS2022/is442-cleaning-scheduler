package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}