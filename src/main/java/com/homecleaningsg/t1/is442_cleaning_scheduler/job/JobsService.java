package com.homecleaningsg.t1.is442_cleaning_scheduler.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Long> getIncompleteJobs(Long employeeId, LocalDateTime timestamp) {
        List<Job> jobs = jobRepository.retrieveJobLogDetails(employeeId, timestamp, "Completed");
        return jobs.stream()
                .map(Job::getJobId)
                .collect(Collectors.toList());
    }
}
