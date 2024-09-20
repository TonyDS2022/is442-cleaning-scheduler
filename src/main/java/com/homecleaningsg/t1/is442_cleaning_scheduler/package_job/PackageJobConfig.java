// PackageJobConfig.java
package com.homecleaningsg.t1.is442_cleaning_scheduler.package_job;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.List;

@Configuration
public class PackageJobConfig {

    @Bean
    CommandLineRunner packageJobCommandLineRunner(PackageJobRepository repository) {
        return args -> {
            PackageJob job1 = new PackageJob(
                new Timestamp(System.currentTimeMillis()), // jobStart
                new Timestamp(System.currentTimeMillis()), // jobEnd
                "Job 1", // jobDescription
                PackageJob.JobStatus.NOT_STARTED // jobStatus
            );
            job1.setRating(PackageJob.Rating.AVERAGE);
            job1.setJobFeedback("Feedback 1");

            PackageJob job2 = new PackageJob(
                new Timestamp(System.currentTimeMillis()), // jobStart
                new Timestamp(System.currentTimeMillis()), // jobEnd
                "Job 2", // jobDescription
                PackageJob.JobStatus.WORKING // jobStatus
            );
            job2.setRating(PackageJob.Rating.GOOD);
            job2.setJobFeedback("Feedback 2");

            repository.saveAll(List.of(job1, job2));
        };
    }
}