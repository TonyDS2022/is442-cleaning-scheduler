package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByWorkerWorkerId(Long workerId);

    @Query("SELECT SUM(s.duration) FROM Shift s WHERE s.worker.workerId = :workerId AND EXTRACT(YEAR FROM s.actualStartDate) = :year")
    Long getWorkerTotalHoursWorkedInYear(@Param("workerId") Long workerId, @Param("year") int year);

    @Query("SELECT SUM(s.duration) FROM Shift s WHERE s.worker.workerId = :workerId AND EXTRACT(YEAR FROM s.actualStartDate) = :year AND EXTRACT(MONTH FROM s.actualStartDate) = :month")
    Long getWorkerTotalHoursWorkedInMonth(@Param("workerId") Long workerId, @Param("year") int year, @Param("month") int month);

    @Query("SELECT s.duration FROM Shift s WHERE s.worker.workerId = :workerId AND s.actualStartDate >= :startOfWeek AND s.actualStartDate < :endOfWeek")
    List<Long> getWorkerTotalHoursWorkedInWeek(
            @Param("workerId") Long workerId,
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek
    );
}