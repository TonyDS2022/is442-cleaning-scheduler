package com.homecleaningsg.t1.is442_cleaning_scheduler.series;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.List;

@Configuration
public class SeriesConfig {

    @Bean
    CommandLineRunner seriesCommandLineRunner(SeriesRepository repository) {
        return args -> {
            Series series1 = new Series();
            series1.setPackageStart(new Timestamp(System.currentTimeMillis()));
            series1.setPackageEnd(new Timestamp(System.currentTimeMillis()));
            series1.setPackageComment("Series 1");
            series1.setOngoing(true);
            series1.setPrice(100.0f);
            series1.setPaxAssigned(2);
            series1.setRooms(1);

            Series series2 = new Series();
            series2.setPackageStart(new Timestamp(System.currentTimeMillis()));
            series2.setPackageEnd(new Timestamp(System.currentTimeMillis()));
            series2.setPackageComment("Series 2");
            series2.setOngoing(false);
            series2.setPrice(200.0f);
            series2.setPaxAssigned(3);
            series2.setRooms(2);

            repository.saveAll(List.of(series1, series2));
        };
    }
}