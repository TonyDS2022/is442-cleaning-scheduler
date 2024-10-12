package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AdminConfig implements CommandLineRunner {

    private final AdminRepository adminRepository;

    public AdminConfig(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Admin ad1 = new Admin("rootadmin", "adminroot1234", true);
        Admin ad2 = new Admin("admin", "admin1234", false);
        adminRepository.saveAll(List.of(ad1, ad2));
    }
}
