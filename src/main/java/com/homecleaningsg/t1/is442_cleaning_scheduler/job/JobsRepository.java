package com.homecleaningsg.t1.is442_cleaning_scheduler.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> retrieveJobLogDetails(Long employeeId, LocalDateTime timestamp, String status);
}
