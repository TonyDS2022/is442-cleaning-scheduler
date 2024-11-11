package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByUsername(String username);

    Worker findByName(String name);

    List<Worker> findByStartWorkingHoursAfterAndEndWorkingHoursBefore(LocalTime startWorkingHours, LocalTime endWorkingHours);

    @Query("SELECT COUNT(DISTINCT w.workerId) FROM Worker w " +
            "WHERE w.joinDate BETWEEN :startOfMonth AND :endOfMonth")
    Long countNewWorkersByMonth(@Param("startOfMonth") LocalDate startOfMonth,
                                @Param("endOfMonth") LocalDate endOfMonth);

    @Query("SELECT COUNT(DISTINCT w.workerId) FROM Worker w " +
            "WHERE w.joinDate <= :endOfMonth " +
            "AND (w.deactivatedAt IS NULL OR w.deactivatedAt > :endOfMonth)")
    Long countExistingWorkersByMonth(@Param("endOfMonth") LocalDate endOfMonth);

    @Query("SELECT COUNT(DISTINCT w.workerId) FROM Worker w " +
            "WHERE w.deactivatedAt IS NOT NULL " +
            "AND w.deactivatedAt BETWEEN :startOfMonth AND :endOfMonth")
    Long countTerminatedWorkersByMonth(@Param("startOfMonth") LocalDate startOfMonth,
                                       @Param("endOfMonth") LocalDate endOfMonth);


    @Query("SELECT COUNT(DISTINCT w.workerId) FROM Worker w " +
            "WHERE EXTRACT(YEAR FROM w.joinDate) = :year")
    Long countNewWorkersByYear(@Param("year") int year);

    @Query("SELECT COUNT(DISTINCT w.workerId) FROM Worker w " +
            "WHERE EXTRACT(YEAR FROM w.joinDate) <= :year " +
            "AND w.isActive " +
            "AND (w.deactivatedAt IS NULL OR EXTRACT(YEAR FROM w.deactivatedAt) > :year)")
    Long countExistingWorkersByYear(@Param("year") int year);

    @Query("SELECT COUNT(DISTINCT w.workerId) FROM Worker w " +
            "WHERE w.deactivatedAt IS NOT NULL " +
            "AND EXTRACT(YEAR FROM w.deactivatedAt) = :year")
    Long countTerminatedWorkersByYear(@Param("year") int year);
}
