package com.homecleaningsg.t1.is442_cleaning_scheduler.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository JobRepository;

    @Autowired
    public JobService(JobRepository JobRepository) {
        this.JobRepository = JobRepository;
    }

    public List<Job> getJob() {
        return JobRepository.findAll();
    }
}