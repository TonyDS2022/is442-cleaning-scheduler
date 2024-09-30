package com.homecleaningsg.t1.is442_cleaning_scheduler.job;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.List;

@Configuration
public class JobConfig {

    @Bean
    CommandLineRunner jobCommandLineRunner(JobRepository repository) {
        return args -> {
            Job job1 = new Job();
            job1.setPackageStart(new Timestamp(System.currentTimeMillis()));
            job1.setPackageEnd(new Timestamp(System.currentTimeMillis()));
            job1.setEmployeeId(100);

            Job job2 = new Job();
            job2.setPackageStart(new Timestamp(System.currentTimeMillis()));
            job2.setPackageEnd(new Timestamp(System.currentTimeMillis()));
            job2.setEmployeeId(200);

            repository.saveAll(List.of(job1, job2));
        };
    }
}