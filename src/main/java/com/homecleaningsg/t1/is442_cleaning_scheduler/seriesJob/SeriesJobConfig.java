//// PackageJobConfig.java
//package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.sql.Timestamp;
//import java.util.List;
//
//@Configuration
//public class SeriesJobConfig {
//
//    @Bean
//    CommandLineRunner seriesJobCommandLineRunner(SeriesJobRepository repository) {
//        return args -> {
//            SeriesJob job1 = new SeriesJob(
//                new Timestamp(System.currentTimeMillis()), // jobStart
//                new Timestamp(System.currentTimeMillis()), // jobEnd
//                "Job 1", // jobDescription
//                SeriesJob.JobStatus.NOT_STARTED // jobStatus
//            );
//            job1.setRating(SeriesJob.Rating.AVERAGE);
//            job1.setJobFeedback("Feedback 1");
//
//            SeriesJob job2 = new SeriesJob(
//                new Timestamp(System.currentTimeMillis()), // jobStart
//                new Timestamp(System.currentTimeMillis()), // jobEnd
//                "Job 2", // jobDescription
//                SeriesJob.JobStatus.WORKING // jobStatus
//            );
//            job2.setRating(SeriesJob.Rating.GOOD);
//            job2.setJobFeedback("Feedback 2");
//
//            repository.saveAll(List.of(job1, job2));
//        };
//    }
//}

package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;

import com.homecleaningsg.t1.is442_cleaning_scheduler.series.Series;
import com.homecleaningsg.t1.is442_cleaning_scheduler.series.SeriesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.List;

@Configuration
public class SeriesJobConfig {

    @Bean
    CommandLineRunner seriesJobCommandLineRunner(SeriesJobRepository seriesJobRepository, SeriesRepository seriesRepository) {
        return args -> {
            // Create a Series instance
            Series series = new Series();
            series.setSeriesTypeId(1);
            series.setGeolocationId(1);
            series.setAcctId(1);
            series.setPackageStart(new Timestamp(System.currentTimeMillis()));
            series.setPackageEnd(new Timestamp(System.currentTimeMillis()));
            series.setPackageComment("Sample Comment");
            series.setOngoing(true);
            series.setPrice(100.0f);
            series.setPaxAssigned(2);
            series.setRooms(3);

            // Save the Series instance
            seriesRepository.save(series);

            // Create SeriesJob instances and set the Series object
            SeriesJob job1 = new SeriesJob(
                    series, // Set the Series object
                    1, // jobId
                    new Timestamp(System.currentTimeMillis()), // jobStart
                    new Timestamp(System.currentTimeMillis()), // jobEnd
                    "Job 1", // jobDescription
                    SeriesJob.JobStatus.NOT_STARTED // jobStatus
            );
            job1.setRating(SeriesJob.Rating.AVERAGE);
            job1.setJobFeedback("Feedback 1");

            SeriesJob job2 = new SeriesJob(
                    series, // Set the Series object
                    2, // jobId
                    new Timestamp(System.currentTimeMillis()), // jobStart
                    new Timestamp(System.currentTimeMillis()), // jobEnd
                    "Job 2", // jobDescription
                    SeriesJob.JobStatus.WORKING // jobStatus
            );
            job2.setRating(SeriesJob.Rating.GOOD);
            job2.setJobFeedback("Feedback 2");

            // Save the SeriesJob instances
            seriesJobRepository.saveAll(List.of(job1, job2));
        };
    }
}