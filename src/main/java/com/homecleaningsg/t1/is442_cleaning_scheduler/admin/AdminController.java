package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(path = "api/v0.1/admins")
public class AdminController {
    private final AdminService adminService;
    private final ShiftService shiftService;

    @Autowired
    public AdminController(AdminService adminService,
                           ShiftService shiftService) {
        this.adminService = adminService;
        this.shiftService = shiftService;
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
    public Long getYearlyHoursOfWorker(@PathVariable("workerId") Long workerId, @PathVariable("year") int year){
        return shiftService.getYearlyHoursOfWorker(workerId, year);
    }

//    localhost:8080/api/v0.1/admins/worker-monthly-hours/1/2024/3
    @GetMapping("/worker-monthly-hours/{workerId}/{year}/{month}")
    public Long getMonthlyHoursOfWorker(
            @PathVariable("workerId") Long workerId,
            @PathVariable("year") int year,
            @PathVariable("month") int month) {
        return shiftService.getMonthlyHoursOfWorker(workerId, year, month);
    }

    //    localhost:8080/api/v0.1/admins/worker-weekly-hours/1/2024-01-01/2024-01-07
    @GetMapping("/worker-weekly-hours/{workerId}/{startOfWeek}/{endOfWeek}")
    public Long getWeeklyHoursOfWorker(
            @PathVariable("workerId") Long workerId,
            @PathVariable("startOfWeek") LocalDate startOfWeek,
            @PathVariable("endOfWeek") LocalDate endOfWeek) {
        return shiftService.getWeeklyHoursOfWorker(workerId, startOfWeek, endOfWeek);
    }
}
