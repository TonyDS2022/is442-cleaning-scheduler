package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
}
