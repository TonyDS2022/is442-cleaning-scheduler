package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByClient(Client client);

    @Query("SELECT COUNT(DISTINCT ct.contractId) FROM Contract ct " +
            "WHERE ct.contractStart BETWEEN :startOfMonth AND :endOfMonth")
    Long countNewContractsByMonth(@Param("startOfMonth") LocalDate startOfMonth,
                                  @Param("endOfMonth") LocalDate endOfMonth);

    // contracts that have started before the end of the month
    // and have not ended before the start of the month
    @Query("SELECT COUNT(DISTINCT ct.contractId) FROM Contract ct " +
            "WHERE ct.contractStart <= :endOfMonth " +
            "AND (ct.contractEnd IS NULL OR ct.contractEnd >= :startOfMonth)")
    Long countExistingContractsByMonth(@Param("startOfMonth") LocalDate startOfMonth,
                                       @Param("endOfMonth") LocalDate endOfMonth);

    @Query("SELECT COUNT(DISTINCT ct.contractId) FROM Contract ct " +
            "WHERE EXTRACT(YEAR FROM ct.contractStart) = :year")
    Long countNewContractsByYear(@Param("year") int year);

    // contracts that have started before the end of the year
    // and have not ended before the beginning of the year
    @Query("SELECT COUNT(DISTINCT ct.contractId) FROM Contract ct " +
            "WHERE EXTRACT(YEAR FROM ct.contractStart) <= :year " +
            "AND (ct.contractEnd IS NULL OR EXTRACT(YEAR FROM ct.contractEnd) >= :year)")
    Long countExistingContractsByYear(@Param("year") int year);

    // New query for counting completed contracts per month
    @Query("SELECT COUNT(ct) FROM Contract ct " +
            "WHERE ct.contractStatus = 'COMPLETED' " +
            "AND ct.contractEnd BETWEEN :startOfMonth AND :endOfMonth")
    Long countCompletedContractsByMonth(@Param("startOfMonth") LocalDate startOfMonth,
                                        @Param("endOfMonth") LocalDate endOfMonth);

    // New query for counting completed contracts per year
    @Query("SELECT COUNT(ct) FROM Contract ct " +
            "WHERE ct.contractStatus = 'COMPLETED' " +
            "AND EXTRACT(YEAR FROM ct.contractEnd) = :year")
    Long countCompletedContractsByYear(@Param("year") int year);
}