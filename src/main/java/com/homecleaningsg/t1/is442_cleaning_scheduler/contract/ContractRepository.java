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

    // contracts that have started before startOfMonth
    // and has not yet ended by startOfMonth
    @Query("SELECT COUNT(DISTINCT ct.contractId) FROM Contract ct " +
            "WHERE ct.contractEnd >= :startOfMonth")
    Long countExistingContractsByMonth(@Param("startOfMonth") LocalDate startOfMonth);

    @Query("SELECT COUNT(DISTINCT ct.contractId) FROM Contract ct " +
            "WHERE EXTRACT (YEAR FROM ct.contractStart) = :year")
    Long countNewContractsByYear(@Param("year") int year);

    @Query("SELECT COUNT(DISTINCT ct.contractId) FROM Contract ct " +
            "WHERE EXTRACT(YEAR FROM ct.contractEnd) >= :year")
    Long countExistingContractsByYear(@Param("year") int year);
}