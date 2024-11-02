package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Collection;
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
    boolean existsByImageHash(String imageHash);

    Optional<LeaveApplication> findTopByWorkerIdAndLeaveTypeAndApplicationStatusOrderByApplicationSubmittedDesc(Long workerId, LeaveType leaveType, ApplicationStatus applicationStatus);

    // Retrieve all pending applications for an admin in the order of workerId
    List<LeaveApplication> findByAdminIdAndApplicationStatusOrderByWorkerId(Long adminId, ApplicationStatus applicationStatus);

    List<LeaveApplication> findByAdminIdAndLeaveStartAfterAndApplicationStatus(Long adminId, OffsetDateTime date, ApplicationStatus status);

    List<LeaveApplication> findByAdminIdAndLeaveEndBeforeAndApplicationStatus(Long adminId, OffsetDateTime date, ApplicationStatus status);

    List<LeaveApplication> findByAdminIdAndLeaveStartBeforeAndLeaveEndAfterAndApplicationStatus(Long adminId, OffsetDateTime startDate, OffsetDateTime endDate, ApplicationStatus status);

    @Query("SELECT l FROM LeaveApplication l WHERE l.adminId = :adminId AND l.applicationStatus = 'APPROVED' AND l.applicationSubmitted = (SELECT MAX(l2.applicationSubmitted) FROM LeaveApplication l2 WHERE l2.workerId = l.workerId AND l2.adminId = :adminId AND l2.applicationStatus = 'APPROVED')")
    List<LeaveApplication> findLatestApprovedLeaveBalanceByAdminId(@Param("adminId") Long adminId);

    Collection<Object> findByWorkerIdAndLeaveTypeAndApplicationStatus(Long workerId, LeaveType leaveType, ApplicationStatus applicationStatus);
}
