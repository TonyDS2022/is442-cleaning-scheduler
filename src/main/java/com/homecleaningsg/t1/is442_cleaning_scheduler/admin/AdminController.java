package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(path = "api/v0.1/admins")
public class AdminController {
    private final AdminService adminService;
    private final ShiftService shiftService;
    private final ClientService clientService;
    private final CleaningSessionService cleaningSessionService;
    private final ContractService contractService;
    private final WorkerService workerService;

    @Autowired
    public AdminController(AdminService adminService,
                           ShiftService shiftService,
                           ClientService clientService,
                           CleaningSessionService cleaningSessionService,
                           ContractService contractService,
                           WorkerService workerService) {
        this.adminService = adminService;
        this.shiftService = shiftService;
        this.clientService = clientService;
        this.cleaningSessionService = cleaningSessionService;
        this.contractService = contractService;
        this.workerService = workerService;
    }

    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }


    @GetMapping("/{username}")
    public Admin getAdminByUsername(@PathVariable("username") String username) {
        return adminService.getAdminByUsername(username);
    }

    // localhost:8080/api/v0.1/admins/worker-yearly-hours/1/2024
    @GetMapping("/worker-yearly-hours/{workerId}/{year}")
    public WorkerHoursDto getYearlyHoursOfWorker(@PathVariable("workerId") Long workerId, @PathVariable("year") int year){
        return shiftService.getYearlyHoursOfWorker(workerId, year);
    }

//    localhost:8080/api/v0.1/admins/worker-monthly-hours/1/2024/3
    @GetMapping("/worker-monthly-hours/{workerId}/{year}/{month}")
    public WorkerHoursDto getMonthlyHoursOfWorker(
            @PathVariable("workerId") Long workerId,
            @PathVariable("year") int year,
            @PathVariable("month") int month) {
        return shiftService.getMonthlyHoursOfWorker(workerId, year, month);
    }

    //    localhost:8080/api/v0.1/admins/worker-weekly-hours/1/2024-01-01/2024-01-07
    @GetMapping("/worker-weekly-hours/{workerId}/{startOfWeek}/{endOfWeek}")
    public WorkerHoursDto getWeeklyHoursOfWorker(
            @PathVariable("workerId") Long workerId,
            @PathVariable("startOfWeek") LocalDate startOfWeek,
            @PathVariable("endOfWeek") LocalDate endOfWeek) {
        return shiftService.getWeeklyHoursOfWorker(workerId, startOfWeek, endOfWeek);
    }

    // localhost:8080/api/v0.1/admins/client-monthly-report/2024/11
    @GetMapping("/client-monthly-report/{year}/{month}")
    public ClientReportDto getMonthlyClientReport(@PathVariable int year, @PathVariable int month) {
        return clientService.getMonthlyClientReport(year, month);
    }

    // localhost:8080/api/v0.1/admins/session-monthly-report/2024/11
    @GetMapping("/session-monthly-report/{year}/{month}")
    public SessionReportDto getMonthlySessionReport(@PathVariable int year, @PathVariable int month) {
        return cleaningSessionService.getMonthlySessionReport(year, month);
    }

    // localhost:8080/api/v0.1/admins/contract-monthly-report/2024/11
    @GetMapping("/contract-monthly-report/{year}/{month}")
    public ContractReportDto getMonthlyContractReport(@PathVariable int year, @PathVariable int month) {
        return contractService.getMonthlyContractReport(year, month);
    }

    // localhost:8080/api/v0.1/admins/worker-monthly-report/2024/11
    @GetMapping("/worker-monthly-report/{year}/{month}")
    public WorkerReportDto getMonthlyWorkerReport(@PathVariable int year, @PathVariable int month) {
        return workerService.getMonthlyWorkerReport(year, month);
    }
}
