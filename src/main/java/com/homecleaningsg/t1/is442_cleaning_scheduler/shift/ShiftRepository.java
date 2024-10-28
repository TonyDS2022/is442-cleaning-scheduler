package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByWorkerWorkerId(Long workerId);

    List<Shift> findBySessionStartTimeBetween(LocalTime start, LocalTime end);

    Shift findLastShiftOnDateByWorkerWorkerId(@NonNull Long workerId, @NonNull LocalDate date);
}