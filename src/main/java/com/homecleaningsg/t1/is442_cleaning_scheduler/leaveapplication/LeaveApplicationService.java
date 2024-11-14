package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor // Lombok annotation to generate constructor with required arguments.
// Removes need for @Autowired and constructor
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final WorkerService workerService;
    private final WorkerRepository workerRepository;
    private final ShiftRepository shiftRepository;

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
            // Retrieve worker's existing shifts during the leave period
            List<Shift> shifts = getShiftsAffectedByLeaveApplication(leaveApplication);
            for (Shift shift: shifts) {
                shift.setWorkerHasPendingLeave(true);
                shiftRepository.save(shift);
                // getPlanningStage in cleaningSession associated with shift will set cleaningsession.PlanningStage to EMBER when it is displayed
            }
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
            // Retrieve worker's existing shifts during the leave period
            List<Shift> shifts = getShiftsAffectedByLeaveApplication(leaveApplication);
            for (Shift shift: shifts) {
                shift.setWorkerHasPendingLeave(true);
                shiftRepository.save(shift);
                // getPlanningStage in cleaningSession associated with shift will set cleaningsession.PlanningStage to EMBER when it is displayed
            }
        } else {
            throw new IllegalArgumentException("Insufficient medical leave balance.");
        }
    }

    public List<Shift> getShiftsAffectedByLeaveApplication(LeaveApplication leaveApplication) {
        LocalDate startDate = leaveApplication.getLeaveStartDate();
        LocalDate endDate = leaveApplication.getLeaveEndDate();
        Long workerId = leaveApplication.getWorker().getWorkerId();
        return shiftRepository.findShiftsByWorkerIdAndOverlappingDateRange(
                workerId,
                startDate,
                endDate
        );
    }

    public List<LeaveApplicationAdminViewDto> getPendingLeaveApplicationsForAdmin(Long adminId) {
        List<LeaveApplication> pendingLeaveApplications = leaveApplicationRepository.findPendingLeaveApplicationsByAdminId(adminId);
        List<LeaveApplicationAdminViewDto> leaveApplicationAdminViewDtos = new ArrayList<>();
        for (LeaveApplication leaveApplication : pendingLeaveApplications) {
            leaveApplicationAdminViewDtos.add(new LeaveApplicationAdminViewDto(leaveApplication));
        }
        return leaveApplicationAdminViewDtos;
    }

}
