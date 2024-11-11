package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
    // Retrieve all pending applications for a specific worker
    List<LeaveApplication> findByWorkerIdAndApplicationStatus(
            Long workerId,
            LeaveApplication.ApplicationStatus applicationStatus
    );

    // Retrieve all non-pending (historical) applications for a specific worker
    List<LeaveApplication> findByWorkerIdAndApplicationStatusNot(
            Long workerId,
            LeaveApplication.ApplicationStatus applicationStatus
    );

    // Retrieve the most recent approved application for a worker
    Optional<LeaveApplication> findTopByWorkerIdAndApplicationStatusOrderByLeaveSubmittedDateDescLeaveSubmittedTimeDesc(
            Long workerId,
            LeaveApplication.ApplicationStatus applicationStatus
    );
    boolean existsByImageHash(String imageHash);

    // Retrieve all pending applications for a specific worker
    Optional<LeaveApplication> findTopByWorkerIdAndLeaveTypeAndApplicationStatusOrderByLeaveSubmittedDateDescLeaveSubmittedTimeDesc(
            Long workerId,
            LeaveType leaveType,
            LeaveApplication.ApplicationStatus applicationStatus
    );

    // Retrieve all pending applications for an admin in the order of workerId
    List<LeaveApplication> findByAdminIdAndApplicationStatusOrderByWorkerId(
            Long adminId,
            LeaveApplication.ApplicationStatus applicationStatus);

    // Retrieve all pending applications after a specific date
    List<LeaveApplication> findByAdminIdAndLeaveStartDateAfterAndApplicationStatus(
            Long adminId,
            LocalDate date,
            LeaveApplication.ApplicationStatus status
    );

    // Retrieve all pending applications before a specific date
    List<LeaveApplication> findByAdminIdAndLeaveEndDateBeforeAndApplicationStatus(
            Long adminId,
            LocalDate date,
            LeaveApplication.ApplicationStatus status
    );

    // Retrieve all pending applications starting before and ending after a specific date
    List<LeaveApplication> findByAdminIdAndLeaveStartDateBeforeAndLeaveEndDateAfterAndApplicationStatus(
            Long adminId,
            LocalDate startDate,
            LocalDate endDate,
            LeaveApplication.ApplicationStatus status
    );

    // retrieves the latest approved leave application for each worker under a specific admin.
    // @Query("SELECT l FROM LeaveApplication l WHERE l.adminId = :adminId AND l.applicationStatus = 'APPROVED' AND l.applicationSubmitted = (SELECT MAX(l2.applicationSubmitted) FROM LeaveApplication l2 WHERE l2.workerId = l.workerId AND l2.adminId = :adminId AND l2.applicationStatus = 'APPROVED')")
    // List<LeaveApplication> findLatestApprovedLeaveBalanceByAdminId(@Param("adminId") Long adminId);
    @Query("SELECT l FROM LeaveApplication l WHERE l.adminId = :adminId AND l.applicationStatus = 'APPROVED' AND (l.leaveSubmittedDate, l.leaveSubmittedTime) = (SELECT MAX(l2.leaveSubmittedDate), MAX(l2.leaveSubmittedTime) FROM LeaveApplication l2 WHERE l2.workerId = l.workerId AND l2.adminId = :adminId AND l2.applicationStatus = 'APPROVED')")
    List<LeaveApplication> findLatestApprovedLeaveBalanceByAdminId(@Param("adminId") Long adminId);

    // Retrieve all pending applications for a specific worker
    Collection<Object> findByWorkerIdAndLeaveTypeAndApplicationStatus(
            Long workerId,
            LeaveType leaveType,
            LeaveApplication.ApplicationStatus applicationStatus
    );

}
