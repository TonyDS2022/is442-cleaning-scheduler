package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.SessionReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.WorkerHoursDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class StatisticsService {

    private final WorkerRepository workerRepository;
    private final ShiftService shiftService;
    private final ClientService clientService;
    private final CleaningSessionService cleaningSessionService;
    private final ContractService contractService;
    private final WorkerService workerService;

    @Autowired
    public StatisticsService(WorkerRepository workerRepository,
                             ShiftService shiftService,
                             ClientService clientService,
                             CleaningSessionService cleaningSessionService,
                             ContractService contractService,
                             WorkerService workerService){
        this.workerRepository = workerRepository;
        this.shiftService = shiftService;
        this.clientService = clientService;
        this.cleaningSessionService = cleaningSessionService;
        this.contractService = contractService;
        this.workerService = workerService;
    }

    private YearlyStatisticsDto getYearlyStatistics(int year){
        List<Worker> workers = workerRepository.findAll();
        List<WorkerHoursDto> workerYearlyHours = new ArrayList<>();
        for(Worker worker: workers){
            Long workerId = worker.getWorkerId();
            WorkerHoursDto workerHours = shiftService.getYearlyHoursOfWorker(workerId, year);
            workerYearlyHours.add(workerHours);
        }

        ClientReportDto clientYearlyReport = clientService.getYearlyClientReport(year);
        SessionReportDto sessionYearlyReport = cleaningSessionService.getYearlySessionReport(year);
        ContractReportDto contractYearlyReport = contractService.getYearlyContractReport(year);
        WorkerReportDto workerYearlyReport = workerService.getYearlyWorkerReport(year);

        return new YearlyStatisticsDto(year, workerYearlyHours, clientYearlyReport, sessionYearlyReport, contractYearlyReport, workerYearlyReport);
    }

    private MonthlyStatisticsDto getMonthlyStatistics(int year, int month){
        List<Worker> workers = workerRepository.findAll();
        List<WorkerHoursDto> workerMonthlyHours = new ArrayList<>();
        for(Worker worker: workers){
            Long workerId = worker.getWorkerId();
            WorkerHoursDto workerHours = shiftService.getMonthlyHoursOfWorker(workerId, year, month);
            workerMonthlyHours.add(workerHours);
        }

        ClientReportDto clientMonthlyReport = clientService.getMonthlyClientReport(year, month);
        SessionReportDto sessionMonthlyReport = cleaningSessionService.getMonthlySessionReport(year, month);
        ContractReportDto contractMonthlyReport = contractService.getMonthlyContractReport(year, month);
        WorkerReportDto workerMonthlyReport = workerService.getMonthlyWorkerReport(year, month);

        return new MonthlyStatisticsDto(year, month, workerMonthlyHours, clientMonthlyReport, sessionMonthlyReport, contractMonthlyReport, workerMonthlyReport);
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
}
