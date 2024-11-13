package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByName(String name);

    @Query("SELECT COUNT(DISTINCT c.clientId) FROM Client c " +
            "WHERE c.joinDate BETWEEN :startOfMonth AND :endOfMonth")
    Long countNewClientsByMonth(@Param("startOfMonth") LocalDate startOfMonth,
                                @Param("endOfMonth") LocalDate endOfMonth);

    @Query("SELECT COUNT(DISTINCT c.clientId) FROM Client c " +
            "WHERE c.joinDate <= :endOfMonth " +
            "AND (c.deactivatedAt IS NULL OR c.deactivatedAt > :endOfMonth)")
    Long countExistingClientsByMonth(@Param("endOfMonth") LocalDate endOfMonth);

    @Query("SELECT COUNT(DISTINCT c.clientId) FROM Client c " +
            "WHERE c.deactivatedAt IS NOT NULL " +
            "AND c.deactivatedAt BETWEEN :startOfMonth AND :endOfMonth")
    Long countTerminatedClientsByMonth(@Param("startOfMonth") LocalDate startOfMonth,
                                       @Param("endOfMonth") LocalDate endOfMonth);

    @Query("SELECT COUNT(DISTINCT c.clientId) FROM Client c " +
            "WHERE EXTRACT(YEAR FROM c.joinDate) = :year")
    Long countNewClientsByYear(@Param("year") int year);

    @Query("SELECT COUNT(DISTINCT c.clientId) FROM Client c " +
            "WHERE EXTRACT(YEAR FROM c.joinDate) <= :year " +
            "AND c.isActive " +
            "AND (c.deactivatedAt IS NULL OR EXTRACT(YEAR FROM c.deactivatedAt) > :year)")
    Long countExistingClientsByYear(@Param("year") int year);

    @Query("SELECT COUNT(DISTINCT c.clientId) FROM Client c " +
            "WHERE c.deactivatedAt IS NOT NULL " +
            "AND EXTRACT(YEAR FROM c.deactivatedAt) = :year")
    Long countTerminatedClientsByYear(@Param("year") int year);

    @Query("SELECT COUNT(DISTINCT c.clientId) FROM Client c ")
    Client findByNameAndPhone(String name, String phone);

    @Query("SELECT c FROM Client c WHERE c.isActive = true")
    List<Client> getClientByActiveEqualsTrue();
}
