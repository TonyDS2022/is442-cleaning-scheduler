package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CleaningSessionRepository extends JpaRepository<CleaningSession, CleaningSessionId> {
    List<CleaningSession> findByContract_ContractId(int contractId);
    Optional<CleaningSession> findByContract_ContractIdAndCleaningSessionId(int contractId, int cleaningSessionId);
}