package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
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
//    // Retrieve all pending applications for a specific worker
//    List<LeaveApplication> findByWorkerIdAndApplicationStatus(
//            Long workerId,
//            LeaveApplication.ApplicationStatus applicationStatus
//    );
//
//    // Retrieve all non-pending (historical) applications for a specific worker
//    List<LeaveApplication> findByWorkerIdAndApplicationStatusNot(
//            Long workerId,
//            LeaveApplication.ApplicationStatus applicationStatus
//    );
//
//    // Retrieve the most recent approved application for a worker
//    Optional<LeaveApplication> findTopByWorkerIdAndApplicationStatusOrderByLeaveSubmittedDateDescLeaveSubmittedTimeDesc(
//            Long workerId,
//            LeaveApplication.ApplicationStatus applicationStatus
//    );
//    boolean existsByImageHash(String imageHash);
//
//    // Retrieve all pending applications for a specific worker
//    Optional<LeaveApplication> findTopByWorkerIdAndLeaveTypeAndApplicationStatusOrderByLeaveSubmittedDateDescLeaveSubmittedTimeDesc(
//            Long workerId,
//            LeaveApplication.LeaveType leaveType,
//            LeaveApplication.ApplicationStatus applicationStatus
//    );
//
//    // Retrieve all pending applications for an admin in the order of workerId
//    List<LeaveApplication> findByAdminIdAndApplicationStatusOrderByWorkerId(
//            Long adminId,
//            LeaveApplication.ApplicationStatus applicationStatus);
//
//    // Retrieve all pending applications after a specific date
//    List<LeaveApplication> findByAdminIdAndLeaveStartDateAfterAndApplicationStatus(
//            Long adminId,
//            LocalDate date,
//            LeaveApplication.ApplicationStatus status
//    );
//
//    // Retrieve all pending applications before a specific date
//    List<LeaveApplication> findByAdminIdAndLeaveEndDateBeforeAndApplicationStatus(
//            Long adminId,
//            LocalDate date,
//            LeaveApplication.ApplicationStatus status
//    );
//
//    // Retrieve all pending applications starting before and ending after a specific date
//    List<LeaveApplication> findByAdminIdAndLeaveStartDateBeforeAndLeaveEndDateAfterAndApplicationStatus(
//            Long adminId,
//            LocalDate startDate,
//            LocalDate endDate,
//            LeaveApplication.ApplicationStatus status
//    );
//
//    // Retrieve all pending applications for a specific worker
//    Collection<Object> findByWorkerIdAndLeaveTypeAndApplicationStatus(
//            Long workerId,
//            LeaveApplication.LeaveType leaveType,
//            LeaveApplication.ApplicationStatus applicationStatus
//    );

    // Retieve all pending and approved applications for a specific worker with leave type and start date after a specific date and end date before a specific date
    @Query("SELECT l " +
            "FROM LeaveApplication l " +
            "WHERE " + "l.worker.workerId = :workerId " +
            "AND l.leaveType = :leaveType " +
            "AND l.leaveStartDate > :leaveStartDate " +
            "AND l.leaveEndDate < :leaveEndDate " +
            "AND l.applicationStatus != :applicationStatus"
    )
    List<LeaveApplication> findByWorkerIdAndLeaveTypeAndLeaveStartDateAfterAndLeaveEndDateBeforeAndApplicationStatusNot(
            Long workerId,
            LeaveApplication.LeaveType leaveType,
            LocalDate leaveStartDate,
            LocalDate leaveEndDate,
            LeaveApplication.ApplicationStatus applicationStatus
    );

    @Query(
            "SELECT COUNT(l) > 0 " +
                    "FROM LeaveApplication l " +
                    "WHERE l.worker.workerId = :workerId " +
                    "AND l.applicationStatus = :status " +
                    "AND (l.leaveStartDate <= :endDate AND l.leaveEndDate >= :startDate)"
    )
    boolean existsByWorkerAndStatusAndDateRangeOverlapping(
            Long workerId,
            LeaveApplication.ApplicationStatus status,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query(
            "SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END " +
                    "FROM LeaveApplication l " +
                    "WHERE l.worker.workerId = :workerId " +
                    "AND (l.applicationStatus = 'PENDING' OR l.applicationStatus = 'APPROVED') " +
                    "AND (l.leaveStartDate <= :rightBound AND l.leaveEndDate >= :leftBound)"
    )
    boolean workerHasPendingOrApprovedLeaveBetween(Long workerId, LocalDate leftBound, LocalDate rightBound);

    @Query(
            "SELECT l " +
                    "FROM LeaveApplication l " +
                    "WHERE l.admin.adminId = :adminId " +
                    "AND (l.applicationStatus = 'PENDING') "
//                    "AND (l.leaveStartDate <= :rightBound AND l.leaveEndDate >= :leftBound)"
    )
    List<LeaveApplication> findPendingLeaveApplicationsByAdminId(Long adminId);

    @Query(
            "SELECT l.worker " +
                    "FROM LeaveApplication l " +
                    "WHERE (l.applicationStatus = 'PENDING' OR l.applicationStatus = 'APPROVED') " +
                    "AND (l.leaveStartDate <= :leaveEndDate AND l.leaveEndDate >= :leaveStartDate)"
    )
    List<Worker> findWorkersByLeaveOverlappingWith(
            @Param("leaveStartDate") LocalDate leaveStartDate,
            @Param("leaveEndDate") LocalDate leaveEndDate
    );
}
