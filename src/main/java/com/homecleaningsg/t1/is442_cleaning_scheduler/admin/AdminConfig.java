package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class AdminConfig {
    @Bean
    CommandLineRunner adminCommandLineRunner(AdminRepository adminRepository) {
        return args -> {
            Admin ad1 = new Admin("rootadmin", "adminroot1234", true);
            Admin ad2 = new Admin("admin", "admin1234", false);
            adminRepository.saveAll(List.of(ad1, ad2));
        };
    }
}
