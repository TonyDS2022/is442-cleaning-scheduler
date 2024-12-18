package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CleaningSessionRepository extends JpaRepository<CleaningSession, Long> {
    List<CleaningSession> findByContract_ContractId(Long contractId);
    Optional<CleaningSession> findByContract_ContractIdAndCleaningSessionId(Long contractId, Long cleaningSessionId);

    @Query("SELECT COUNT(DISTINCT cs.cleaningSessionId) " +
            "FROM CleaningSession cs " +
            "WHERE cs.sessionStatus = 'FINISHED' " +
            "AND cs.sessionEndDate BETWEEN :startOfMonth AND :endOfMonth")
    Long countNoOfMonthFinishedSessions(@Param("startOfMonth") LocalDate startOfMonth,
                                        @Param("endOfMonth") LocalDate endOfMonth);

    @Query("SELECT COUNT(DISTINCT cs.cleaningSessionId) " +
            "FROM CleaningSession cs " +
            "WHERE cs.sessionStatus = 'CANCELLED' " +
            "AND cs.cancelledAt IS NOT NULL " +
            "AND cs.cancelledAt BETWEEN :startOfMonth AND :endOfMonth")
    Long countNoOfMonthCancelledSessions(@Param("startOfMonth") LocalDate startOfMonth,
                                         @Param("endOfMonth") LocalDate endOfMonth);

    @Query("SELECT COUNT(DISTINCT cs.cleaningSessionId) " +
            "FROM CleaningSession cs " +
            "WHERE cs.sessionStatus = 'FINISHED' " +
            "AND EXTRACT(YEAR FROM cs.sessionEndDate) = :year")
    Long countNoOfYearFinishedSessions(@Param("year") int year);

    @Query("SELECT COUNT(DISTINCT cs.cleaningSessionId) " +
            "FROM CleaningSession cs " +
            "WHERE cs.sessionStatus = 'CANCELLED' " +
            "AND cs.cancelledAt IS NOT NULL " +
            "AND EXTRACT(YEAR FROM cs.cancelledAt) = :year")
    Long countNoOfYearCancelledSessions(@Param("year") int year);
}
