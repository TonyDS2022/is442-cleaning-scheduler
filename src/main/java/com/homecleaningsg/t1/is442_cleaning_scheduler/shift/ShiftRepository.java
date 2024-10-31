package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByWorkerWorkerId(Long workerId);

    List<Shift> findBySessionStartTimeBetween(LocalTime start, LocalTime end);

    @Query("SELECT s FROM Shift s WHERE s.worker.workerId = :workerId AND s.sessionStartDate = :date ORDER BY s.sessionEndTime DESC")
    Shift findLastShiftOnDateByWorkerWorkerId(@Param("workerId") Long workerId, @Param("date") LocalDate date);

    // get shifts by worker and month / week / day
    @Query("SELECT s FROM Shift s WHERE MONTH(s.sessionStartDate) = :month AND YEAR(s.sessionStartDate) = :year AND s.worker.workerId = :workerId")
    List<Shift> findByMonthAndWorker(@Param("month") int month, @Param("year") int year, @Param("workerId") Long workerId);

    @Query("SELECT s FROM Shift s WHERE WEEK(s.sessionStartDate) = :week AND YEAR(s.sessionStartDate) = :year AND s.worker.workerId = :workerId")
    List<Shift> findByWeekAndWorker(@Param("week") int week, @Param("year") int year, @Param("workerId") Long workerId);

    @Query("SELECT s FROM Shift s WHERE s.sessionStartDate = :date AND s.worker.workerId = :workerId")
    List<Shift> findByDayAndWorker(@Param("date") LocalDate date, @Param("workerId") Long workerId);

    @Query("SELECT s FROM Shift s WHERE s.sessionStartDate = :date AND s.worker.workerId = :workerId AND s.sessionEndTime < :time ORDER BY s.sessionEndTime DESC")
    List<Shift> findLastShiftByDayAndWorkerBeforeTime(@Param("workerId") Long workerId,
                                                          @Param("date") LocalDate date,
                                                          @Param("time") LocalTime time);

}