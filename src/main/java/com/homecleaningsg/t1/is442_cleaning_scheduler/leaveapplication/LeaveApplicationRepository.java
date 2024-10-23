package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
    // Retrieve all pending applications for a specific worker
    List<LeaveApplication> findByWorkerIdAndApplicationStatus(Long workerId, ApplicationStatus applicationStatus);

    // Retrieve all non-pending (historical) applications for a specific worker
    List<LeaveApplication> findByWorkerIdAndApplicationStatusNot(Long workerId, ApplicationStatus applicationStatus);

    // Retrieve the most recent approved application for a worker
    Optional<LeaveApplication> findTopByWorkerIdAndApplicationStatusOrderByApplicationSubmittedDesc(Long workerId, ApplicationStatus applicationStatus);
}
