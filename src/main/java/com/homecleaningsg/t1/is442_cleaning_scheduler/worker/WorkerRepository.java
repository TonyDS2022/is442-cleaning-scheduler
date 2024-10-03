package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByUsername(String username);
}
