package com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication;

import com.homecleaningsg.t1.is442_cleaning_scheduler.admin.LeaveTakenDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftWithWorkerDetailsDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.WorkerHoursDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication.LeaveType.ANNUAL;
import static com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication.LeaveType.MEDICAL;

@Service
@RequiredArgsConstructor // Lombok annotation to generate constructor with required arguments.
// Removes need for @Autowired and constructor
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final WorkerService workerService;
    private final WorkerRepository workerRepository;
    private final ShiftRepository shiftRepository;

    public List<LeaveApplicationDto> getAllLeaveApplication(){
        List<LeaveApplication> leaveApplications = leaveApplicationRepository.findAll();
        List<LeaveApplicationDto> leaveApplicationDtos = new ArrayList<>();
        for (LeaveApplication leaveApplication: leaveApplications) {
            LeaveApplicationDto leaveApplicationDto = new LeaveApplicationDto(
                    leaveApplication.getWorker().getWorkerId(),
                    leaveApplication.getWorker().getName(),
                    leaveApplication.getAdmin().getAdminId(),
                    leaveApplication.getAdmin().getUsername(),
                    leaveApplication.getLeaveType(),
                    leaveApplication.getLeaveStartDate(),
                    leaveApplication.getLeaveEndDate(),
                    leaveApplication.getApplicationStatus(),
                    leaveApplication.getLeaveSubmittedDate(),
                    leaveApplication.getLeaveSubmittedTime());
            leaveApplicationDtos.add(leaveApplicationDto);
        }
        return leaveApplicationDtos;
    }

    public void applyAnnualLeave(
            Long workerId,
            LocalDate startDate,
            LocalDate endDate
    ) throws IllegalArgumentException {
        if (workerService.workerCanApplyForLeave(workerId, startDate, endDate, ANNUAL)) {
            // Create and save leave application in database
            Worker worker = workerRepository.findById(workerId).orElseThrow(
                    () -> new IllegalArgumentException("Worker not found.")
            );
            LeaveApplication leaveApplication = new LeaveApplication(
                    worker,
                    ANNUAL,
                    startDate,
                    endDate
            );
            leaveApplicationRepository.save(leaveApplication);
            // Retrieve worker's existing shifts during the leave period
            List<AffectedShiftDto> shifts = getShiftsAffectedByLeaveApplication(leaveApplication);
            for (AffectedShiftDto affectedShiftDto: shifts) {
                Long shiftId = affectedShiftDto.shiftId;
                Shift shift = shiftRepository.findById(shiftId).orElseThrow(() -> new IllegalArgumentException("Shift not found"));
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
            List<AffectedShiftDto> shifts = getShiftsAffectedByLeaveApplication(leaveApplication);
            for (AffectedShiftDto affectedShiftDto: shifts) {
                Long shiftId = affectedShiftDto.shiftId;
                Shift shift = shiftRepository.findById(shiftId).orElseThrow(() -> new IllegalArgumentException("Shift not found"));
                shift.setWorkerHasPendingLeave(true);
                shiftRepository.save(shift);
                // getPlanningStage in cleaningSession associated with shift will set cleaningsession.PlanningStage to EMBER when it is displayed
            }
        } else {
            throw new IllegalArgumentException("Insufficient medical leave balance.");
        }
    }

    public List<AffectedShiftDto> getShiftsAffectedByLeaveApplication(LeaveApplication leaveApplication) {
        LocalDate startDate = leaveApplication.getLeaveStartDate();
        LocalDate endDate = leaveApplication.getLeaveEndDate();
        Long workerId = leaveApplication.getWorker().getWorkerId();
        List<Shift> affectedShifts = shiftRepository.findShiftsByWorkerIdAndOverlappingDateRange(
                workerId,
                startDate,
                endDate
        );
        List<AffectedShiftDto> affectedShiftDto = new ArrayList<>();
        for (Shift shift: affectedShifts){
            affectedShiftDto.add(new AffectedShiftDto(shift));
        }
        return affectedShiftDto;
    }

    public List<LeaveApplicationAdminViewDto> getPendingLeaveApplicationsForAdmin(Long adminId) {
        // Fetch pending leave applications for the admin
        List<LeaveApplication> pendingLeaveApplications = leaveApplicationRepository.findPendingLeaveApplicationsByAdminId(adminId);

        // List to hold the final result
        List<LeaveApplicationAdminViewDto> leaveApplicationAdminViewDtos = new ArrayList<>();

        for (LeaveApplication leaveApplication : pendingLeaveApplications) {

            // Fetch affected shifts for the current leave application
            List<AffectedShiftDto> affectedShifts = getShiftsAffectedByLeaveApplication(leaveApplication);

            // Instantiate the DTO
            LeaveApplicationAdminViewDto dto = new LeaveApplicationAdminViewDto(leaveApplication,affectedShifts);

            // Add the populated DTO to the result list
            leaveApplicationAdminViewDtos.add(dto);
        }

        return leaveApplicationAdminViewDtos;
    }

    public Long getWorkerTotalLeaveTakenInMonth(Long workerId,
                                                int year,
                                                int month,
                                                LeaveApplication.LeaveType leaveType){
        LocalDate startOfMonth = YearMonth.of(year, month).atDay(1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        Long totalLeaveTaken = 0L;
        List<LeaveApplication> leaveApplications = leaveApplicationRepository.findByWorkerIdAndMonth(workerId, year, month, leaveType, LeaveApplication.ApplicationStatus.REJECTED);
        for(LeaveApplication leaveApplication: leaveApplications){
            LocalDate leaveStartDate = leaveApplication.getLeaveStartDate();
            LocalDate leaveEndDate = leaveApplication.getLeaveEndDate();
            if(leaveStartDate.getMonthValue() == month && leaveEndDate.getMonthValue() == month){
                totalLeaveTaken += ChronoUnit.DAYS.between(leaveStartDate, leaveEndDate) + 1;
            } else if (leaveStartDate.getMonthValue() == month) {
                // Only the start date is in the requested month
                totalLeaveTaken += ChronoUnit.DAYS.between(leaveStartDate, endOfMonth) + 1; // +1 to include the start date
            } else if (leaveEndDate.getMonthValue() == month) {
                // Only the end date is in the requested month
                totalLeaveTaken += ChronoUnit.DAYS.between(startOfMonth, leaveEndDate) + 1; // +1 to include the end date
            }
        }
        return totalLeaveTaken;
    }

    public LeaveTakenDto getMonthlyLeaveTakenOfWorker(Long workerId, int year, int month){
        Long annualLeaveTaken = getWorkerTotalLeaveTakenInMonth(workerId, year, month, ANNUAL);
        if (annualLeaveTaken == null) {
            annualLeaveTaken = 0L;
        }
        Long medicalLeaveTaken = getWorkerTotalLeaveTakenInMonth(workerId, year, month, MEDICAL);
        if (medicalLeaveTaken == null) {
            medicalLeaveTaken = 0L;
        }
        return new LeaveTakenDto(workerId, annualLeaveTaken, medicalLeaveTaken);
    }
}
