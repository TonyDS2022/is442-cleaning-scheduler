package com.homecleaningsg.t1.is442_cleaning_scheduler.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/incomplete")
    public List<Long> getIncompleteJobs(
            @RequestParam Long employee_id,
            @RequestParam String timestamp
    ) {
        LocalDateTime logTimestamp = LocalDateTime.parse(timestamp);
        return jobService.getIncompleteJobs(employee_id, logTimestamp);
    }
}
