package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.AvailableWorkerDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminService {

    private final ShiftService shiftService;
    private final WorkerRepository workerRepository;
    private final CleaningSessionRepository cleaningSessionRepository;
    @Value("${shift.auto-assign-worker}")
    private boolean autoAssignWorker;

    private final AdminRepository adminRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final ShiftRepository shiftRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository,
                        LeaveApplicationRepository leaveApplicationRepository, ShiftRepository shiftRepository, ShiftService shiftService, WorkerRepository workerRepository, CleaningSessionRepository cleaningSessionRepository) {

        this.adminRepository = adminRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.shiftRepository = shiftRepository;
        this.shiftService = shiftService;
        this.workerRepository = workerRepository;
        this.cleaningSessionRepository = cleaningSessionRepository;
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username).orElse(null);
    }

    public Admin addAdmin(Admin admin){
        admin.setJoinDate(LocalDate.now());
        return adminRepository.save(admin);
    }

    public Admin updateAdmin(Long adminId, Admin updatedAdmin){
        if(!adminRepository.existsById(adminId)){
            throw new IllegalArgumentException("Admin not found");
        }
        updatedAdmin.setAdminId(adminId);
        return adminRepository.save(updatedAdmin);
    }

    public Admin deactivateAdmin(Long adminId){
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        admin.setActive(false);
        admin.setDeactivatedAt(LocalDate.now());
        return adminRepository.save(admin);
    }

    public void approveLeaveApplication(Long LeaveApplicationId){
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(LeaveApplicationId)
                .orElseThrow(() -> new IllegalArgumentException("Leave Application not found"));
        approveLeaveApplication(leaveApplication);
    }

    @Transactional
    public void approveLeaveApplication(LeaveApplication leaveApplication) {
        // Approve leave application status
        leaveApplication.setApplicationStatus(LeaveApplication.ApplicationStatus.APPROVED);
        leaveApplicationRepository.save(leaveApplication);

        // Get affected shifts due to approved leave
        List<Shift> shifts = shiftRepository.findShiftsByWorkerIdAndOverlappingDateRange(
                leaveApplication.getWorker().getWorkerId(),
                leaveApplication.getLeaveStartDate(),
                leaveApplication.getLeaveEndDate()
        );

        for (Shift shift : shifts) {
            CleaningSession cleaningSession = shift.getCleaningSession();

            if (autoAssignWorker) {
                List<AvailableWorkerDto> availableWorkers = shiftService.getAvailableWorkersForShift(shift);
                if (!availableWorkers.isEmpty()) {
                    // Try assigning the first available worker
                    Worker worker = workerRepository.findById(availableWorkers.get(0).getWorkerId())
                            .orElse(null);
                    if (worker != null) {
                        shift.setWorker(worker);
                        cleaningSession.setPlanningStage(CleaningSession.PlanningStage.GREEN);
                    } else {
                        cleaningSession.setPlanningStage(CleaningSession.PlanningStage.RED);
                    }
                } else {
                    cleaningSession.setPlanningStage(CleaningSession.PlanningStage.RED);
                }
            } else {
                // No reassignment; just set planning stage to RED
                shift.setWorker(null);
                cleaningSession.setPlanningStage(CleaningSession.PlanningStage.RED);
            }

            // Save updates for each shift and session
            shiftRepository.save(shift);
            cleaningSessionRepository.save(cleaningSession);
        }

        // Save leave application status only once, after processing
        leaveApplicationRepository.save(leaveApplication);
    }


    public void rejectLeaveApplication(Long LeaveApplicationId){
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(LeaveApplicationId)
                .orElseThrow(() -> new IllegalArgumentException("Leave Application not found"));
        rejectLeaveApplication(leaveApplication);
    }

    public void rejectLeaveApplication(LeaveApplication leaveApplication){
        // admin rejects leave
        leaveApplication.setApplicationStatus(LeaveApplication.ApplicationStatus.REJECTED);
        // retrieve affected shifts
        List<Shift> shifts = shiftRepository.findShiftsByWorkerIdAndOverlappingDateRange(
                leaveApplication.getWorker().getWorkerId(),
                leaveApplication.getLeaveStartDate(),
                leaveApplication.getLeaveEndDate()
        );
        // for each affected shift, set planning stage back to GREEN
        for (Shift shift : shifts) {
            CleaningSession cleaningSession = shift.getCleaningSession();
            // check if all shifts are assigned to workers
            cleaningSession.setPlanningStage(CleaningSession.PlanningStage.GREEN);
            cleaningSessionRepository.save(cleaningSession);
        }
        leaveApplicationRepository.save(leaveApplication);
    }

}
