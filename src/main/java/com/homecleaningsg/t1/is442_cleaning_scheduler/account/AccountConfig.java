package com.homecleaningsg.t1.is442_cleaning_scheduler.account;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class AccountConfig {
    @Bean
    CommandLineRunner accountCommandLineRunner(AccountRepository accountRepository) {
        return args -> {
            Account acct1 = new Account("testuser1","karthigamagesh17@gmail.com","testuser123","99999999","test user 1");
            Account acct2 = new Account("testuser2","karthigamagesh17@gmail.com","testuser223","99999999","test user 2");

            System.out.println(acct1);
            System.out.println(acct2);
            accountRepository.saveAll(List.of(acct1, acct2));

        };
    }
}
