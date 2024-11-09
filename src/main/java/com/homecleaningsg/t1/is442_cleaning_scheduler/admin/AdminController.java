package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(path = "api/v0.1/admins")
public class AdminController {
    private final AdminService adminService;
    private final StatisticsService statisticsService;

    @Autowired
    public AdminController(AdminService adminService,
                           StatisticsService statisticsService) {
        this.adminService = adminService;
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }


    @GetMapping("/{username}")
    public Admin getAdminByUsername(@PathVariable("username") String username) {
        return adminService.getAdminByUsername(username);
    }

    // localhost:8080/api/v0.1/admins/yearly-statistics/2024
    @GetMapping("/yearly-statistics/{year}")
    public StatisticsReportDto getStatisticsByYear(
            @PathVariable("year") int year) {
        return statisticsService.getStatisticsByYear(year);
    }
}
