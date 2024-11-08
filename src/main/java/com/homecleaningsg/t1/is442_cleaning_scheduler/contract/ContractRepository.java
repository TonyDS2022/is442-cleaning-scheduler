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

    @Query("SELECT COUNT(DISTINCT ct.contractId) FROM Contract ct WHERE ct.creationDate BETWEEN :startOfMonth AND :endOfMonth")
    Long countNewContractsByMonth(@Param("startOfMonth") LocalDate startOfMonth,
                                  @Param("endOfMonth") LocalDate endOfMonth);

    // contracts that have been created before startOfMonth
    // and has not yet ended by startOfMonth
    @Query("SELECT COUNT(DISTINCT ct.contractId) FROM Contract ct WHERE ct.creationDate < :startOfMonth AND ct.contractEnd > :startOfMonth")
    Long countExistingOngoingContractsByMonth(@Param("startOfMonth") LocalDate startOfMonth);
}