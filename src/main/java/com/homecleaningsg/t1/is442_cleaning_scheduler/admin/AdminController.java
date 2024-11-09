package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.SessionReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.WorkerHoursDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.*;
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
}
