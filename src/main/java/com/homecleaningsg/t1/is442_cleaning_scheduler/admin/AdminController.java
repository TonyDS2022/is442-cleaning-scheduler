package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
