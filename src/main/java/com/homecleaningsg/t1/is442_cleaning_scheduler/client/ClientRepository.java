package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByName(String name);

    @Query("SELECT COUNT(DISTINCT c.clientId) FROM Client c WHERE c.joinDate BETWEEN :startOfMonth AND :endOfMonth")
    Long countNewClientsByMonth(@Param("startOfMonth") LocalDate startOfMonth,
                                @Param("endOfMonth") LocalDate endOfMonth);

    @Query("SELECT COUNT(DISTINCT c.clientId) FROM Client c WHERE c.joinDate < :startOfMonth")
    Long countExistingClientsByMonth(@Param("startOfMonth") LocalDate startOfMonth);

    @Query("SELECT COUNT(DISTINCT c.clientId) FROM Client c WHERE c.deactivatedAt IS NOT NULL AND c.deactivatedAt BETWEEN :startOfMonth AND :endOfMonth")
    Long countTerminatedClientsByMonth(@Param("startOfMonth") LocalDate startOfMonth,
                                       @Param("endOfMonth") LocalDate endOfMonth);
}
