package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class WorkerConfig {
    @Bean
    CommandLineRunner workerCommandLineRunner(WorkerRepository workerRepository) {
        return args -> {
            Worker ad1 = new Worker("Karthiga","karthiga","kar1243","karthigamagesh17@gmail.com", "99999999", "Hello, I am a sincere cleaner specialised in wooden floors", "8am-5pm");
            Worker ad2 = new Worker("John","john","john1243","john@gmail.com", "99999999", "Cleaner with 20 years experience in cleaning bungalows/landed properties", "1pm-10pm");
            workerRepository.saveAll(List.of(ad1, ad2));
        };
    }
}
