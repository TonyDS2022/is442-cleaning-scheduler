package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    List<Shift> findByWorkerWorkerId(Long workerId);
}