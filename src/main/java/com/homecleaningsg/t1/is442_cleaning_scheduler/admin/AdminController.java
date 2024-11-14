package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionUpdateDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.AbstractDocument;
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

    @PostMapping("/add-admin/")
    public ResponseEntity<String> addAdmin(@RequestBody Admin admin) {
        try {
            adminService.addAdmin(admin);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Admin added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add admin.");
        }
    }

    @PutMapping("/update-admin/{adminId}")
    public ResponseEntity<String> updateAdmin(
            @PathVariable("adminId") Long adminId, @RequestBody Admin updatedAdmin) {
        try {
            adminService.updateAdmin(adminId, updatedAdmin);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Admin updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update admin details.");
        }
    }

    // localhost:8080/api/v0.1/admin/deactivate-admin/1
    @PutMapping("/deactivate-admin/{adminId}")
    public ResponseEntity<String> deactivateAdmin(@PathVariable("adminId") Long adminId) {
        try {
            adminService.deactivateAdmin(adminId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Admin deactivated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to deactivate admin.");
        }
    }

    // localhost:8080/api/v0.1/admins/yearly-statistics/2024
    @GetMapping("/yearly-statistics/{year}")
    public StatisticsReportDto getStatisticsByYear(
            @PathVariable("year") int year) {
        return statisticsService.getStatisticsByYear(year);
    }

//    localhost:8080/api/v0.1/admins/download-statistics/2024
    @GetMapping("/download-statistics/{year}")
    public ResponseEntity<byte[]> downloadStatisticsReport(@PathVariable("year") int year) {
        // Get the data for the year
        StatisticsReportDto statisticsData = statisticsService.getStatisticsByYear(year);

        // Convert data to CSV
        byte[] csvData = CSVExporter.exportStatisticsToCsv(
                statisticsData.getYearlyStats(),
                statisticsData.getMonthlyStats(),
                statisticsData.getWeeklyStats());

        // Set response headers for file download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "statistics_report_" + year + ".csv");

        // Return the CSV file in the response
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }

    // localhost:8080/api/v0.1/admins/approve-leave-application/
    @PutMapping("/approve-leave-application/")
    public ResponseEntity<String> approveLeaveApplication(@RequestBody LeaveApplication leaveApplication){
        try{
            adminService.approveLeaveApplication(leaveApplication);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Leave application approved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to approved leave application.");
        }
    }

    @PutMapping("/reject-leave-application/")
    public ResponseEntity<String> rejectLeaveApplication(@RequestBody LeaveApplication leaveApplication){
        try{
            adminService.rejectLeaveApplication(leaveApplication);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Leave application rejected successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to rejected leave application.");
        }
    }
}
