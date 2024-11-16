package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.SessionReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.WorkerHoursDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService {

    private final WorkerRepository workerRepository;
    private final ShiftService shiftService;
    private final ClientService clientService;
    private final CleaningSessionService cleaningSessionService;
    private final ContractService contractService;
    private final WorkerService workerService;
    private final LeaveApplicationService leaveApplicationService;

    @Autowired
    public StatisticsService(WorkerRepository workerRepository,
                             ShiftService shiftService,
                             ClientService clientService,
                             CleaningSessionService cleaningSessionService,
                             ContractService contractService,
                             WorkerService workerService,
                             LeaveApplicationService leaveApplicationService){
        this.workerRepository = workerRepository;
        this.shiftService = shiftService;
        this.clientService = clientService;
        this.cleaningSessionService = cleaningSessionService;
        this.contractService = contractService;
        this.workerService = workerService;
        this.leaveApplicationService = leaveApplicationService;
    }

    private YearlyStatisticsDto getYearlyStatistics(int year){
        List<Worker> workers = workerRepository.findAll();
        List<WorkerHoursDto> workerYearlyHours = new ArrayList<>();
        for(Worker worker: workers){
            Long workerId = worker.getWorkerId();
            WorkerHoursDto workerHours = shiftService.getYearlyHoursOfWorker(workerId, year);
            workerYearlyHours.add(workerHours);
        }

        List<LeaveBalanceDto> workerYearlyLeaveBalance = new ArrayList<>();
        for(Worker worker: workers){
            Long workerId = worker.getWorkerId();
            Long workerAnnualLeaveBalance = workerService.getWorkerLeaveBalance(workerId, LeaveApplication.LeaveType.ANNUAL, year);
            Long workerMedicalLeaveBalance = workerService.getWorkerLeaveBalance(workerId, LeaveApplication.LeaveType.MEDICAL, year);
            LeaveBalanceDto workerLeaveBalance = new LeaveBalanceDto(workerId, workerAnnualLeaveBalance, workerMedicalLeaveBalance);
            workerYearlyLeaveBalance.add(workerLeaveBalance);
        }

        ClientReportDto clientYearlyReport = clientService.getYearlyClientReport(year);
        SessionReportDto sessionYearlyReport = cleaningSessionService.getYearlySessionReport(year);
        ContractReportDto contractYearlyReport = contractService.getYearlyContractReport(year);
        WorkerReportDto workerYearlyReport = workerService.getYearlyWorkerReport(year);

        return new YearlyStatisticsDto(year, workerYearlyHours, workerYearlyLeaveBalance, clientYearlyReport, sessionYearlyReport, contractYearlyReport, workerYearlyReport);
    }

    private MonthlyStatisticsDto getMonthlyStatistics(int year, int month){
        List<Worker> workers = workerRepository.findAll();
        List<WorkerHoursDto> workerMonthlyHours = new ArrayList<>();
        for(Worker worker: workers){
            Long workerId = worker.getWorkerId();
            WorkerHoursDto workerHours = shiftService.getMonthlyHoursOfWorker(workerId, year, month);
            workerMonthlyHours.add(workerHours);
        }
        List<LeaveTakenDto> workerMonthlyLeaveTaken = new ArrayList<>();
        for(Worker worker: workers){
            Long workerId = worker.getWorkerId();
            LeaveTakenDto workerLeaveTaken = leaveApplicationService.getMonthlyLeaveTakenOfWorker(workerId, year, month);
            workerMonthlyLeaveTaken.add(workerLeaveTaken);
        }

        ClientReportDto clientMonthlyReport = clientService.getMonthlyClientReport(year, month);
        SessionReportDto sessionMonthlyReport = cleaningSessionService.getMonthlySessionReport(year, month);
        ContractReportDto contractMonthlyReport = contractService.getMonthlyContractReport(year, month);
        WorkerReportDto workerMonthlyReport = workerService.getMonthlyWorkerReport(year, month);

        return new MonthlyStatisticsDto(year, month, workerMonthlyHours, workerMonthlyLeaveTaken, clientMonthlyReport, sessionMonthlyReport, contractMonthlyReport, workerMonthlyReport);
    }

    private WeeklyStatisticsDto getWeeklyStatistics(LocalDate startOfWeek, LocalDate endOfWeek){
        List<Worker> workers = workerRepository.findAll();
        List<WorkerHoursDto> workerWeeklyHours = new ArrayList<>();
        for(Worker worker: workers){
            Long workerId = worker.getWorkerId();
            WorkerHoursDto workerHours = shiftService.getWeeklyHoursOfWorker(workerId, startOfWeek, endOfWeek);
            workerWeeklyHours.add(workerHours);
        }

        return new WeeklyStatisticsDto(startOfWeek, endOfWeek, workerWeeklyHours);
    }

    public StatisticsReportDto getStatisticsByYear(int year) {
        LocalDate today = LocalDate.now();

        // If it's the current year, stop at the current date; otherwise, go until the end of the year
        boolean isCurrentYear = (year == today.getYear());
        LocalDate endDate = isCurrentYear ? today : LocalDate.of(year, 12, 31);

        // Yearly statistics
        YearlyStatisticsDto yearlyStats = getYearlyStatistics(year);

        // Monthly statistics up to the current month if it's the current year, otherwise for all months
        List<MonthlyStatisticsDto> monthlyStats = new ArrayList<>();
        int lastMonth = isCurrentYear ? today.getMonthValue() : 12;
        for (int month = 1; month <= lastMonth; month++) {
            monthlyStats.add(getMonthlyStatistics(year, month));
        }

        // Weekly statistics up to the current week if it's the current year, otherwise for all weeks
        List<WeeklyStatisticsDto> weeklyStats = new ArrayList<>();

        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfWeek = startOfYear.with(DayOfWeek.SUNDAY);

        while (!endOfWeek.isAfter(endDate)) {
            LocalDate startOfWeek = endOfWeek.minusDays(6);
            weeklyStats.add(getWeeklyStatistics(startOfWeek, endOfWeek));
            endOfWeek = endOfWeek.plusWeeks(1);
        }

        return new StatisticsReportDto(year, yearlyStats, monthlyStats, weeklyStats);
    }
}
