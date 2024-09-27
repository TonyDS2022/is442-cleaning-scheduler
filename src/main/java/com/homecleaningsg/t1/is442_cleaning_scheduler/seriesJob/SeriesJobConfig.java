// PackageJobConfig.java
package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.List;

@Configuration
public class SeriesJobConfig {

    @Bean
    CommandLineRunner seriesJobCommandLineRunner(SeriesJobRepository repository) {
        return args -> {
            SeriesJob job1 = new SeriesJob(
                new Timestamp(System.currentTimeMillis()), // jobStart
                new Timestamp(System.currentTimeMillis()), // jobEnd
                "Job 1", // jobDescription
                SeriesJob.JobStatus.NOT_STARTED // jobStatus
            );
            job1.setRating(SeriesJob.Rating.AVERAGE);
            job1.setJobFeedback("Feedback 1");

            SeriesJob job2 = new SeriesJob(
                new Timestamp(System.currentTimeMillis()), // jobStart
                new Timestamp(System.currentTimeMillis()), // jobEnd
                "Job 2", // jobDescription
                SeriesJob.JobStatus.WORKING // jobStatus
            );
            job2.setRating(SeriesJob.Rating.GOOD);
            job2.setJobFeedback("Feedback 2");

            repository.saveAll(List.of(job1, job2));
        };
    }
}