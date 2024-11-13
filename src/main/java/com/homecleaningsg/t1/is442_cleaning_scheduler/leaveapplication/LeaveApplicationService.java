package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor // Lombok annotation to generate constructor with required arguments.
// Removes need for @Autowired and constructor
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final WorkerService workerService;
    private final WorkerRepository workerRepository;

    public void applyAnnualLeave(
            Long workerId,
            LocalDate startDate,
            LocalDate endDate
    ) throws IllegalArgumentException {
        if (workerService.workerCanApplyForLeave(workerId, startDate, endDate, LeaveApplication.LeaveType.ANNUAL)) {
            // Create and save leave application in database
            Worker worker = workerRepository.findById(workerId).orElseThrow(
                    () -> new IllegalArgumentException("Worker not found.")
            );
            LeaveApplication leaveApplication = new LeaveApplication(
                    worker,
                    LeaveApplication.LeaveType.ANNUAL,
                    startDate,
                    endDate
            );
            leaveApplicationRepository.save(leaveApplication);
        } else {
            throw new IllegalArgumentException("Insufficient annual leave balance.");
        }
    }

    public void applyMedicalLeave(
            Long workerId,
            LocalDate startDate,
            LocalDate endDate
    ) throws IllegalArgumentException {
        if (workerService.workerCanApplyForLeave(workerId, startDate, endDate, LeaveApplication.LeaveType.MEDICAL)) {
            // Create and save leave application in database
            Worker worker = workerRepository.findById(workerId).orElseThrow(
                    () -> new IllegalArgumentException("Worker not found.")
            );
            LeaveApplication leaveApplication = new LeaveApplication(
                    worker,
                    LeaveApplication.LeaveType.MEDICAL,
                    startDate,
                    endDate
            );
            leaveApplicationRepository.save(leaveApplication);
        } else {
            throw new IllegalArgumentException("Insufficient medical leave balance.");
        }
    }

}
