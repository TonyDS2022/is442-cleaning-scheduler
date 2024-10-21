package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class WorkerConfig implements CommandLineRunner{

    private final WorkerRepository workerRepository;

    public WorkerConfig(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;


        Worker worker1 = new Worker(
                "Karthiga",
                "karthiga",
                "kar1243",
                "karthigamagesh17@gmail.com",
                "99999999",
                "Hello, I am a sincere cleaner specialised in wooden floors",
                LocalTime.of(8,0),
                LocalTime.of(17,0));
        Worker worker2 = new Worker(
                "John",
                "john",
                "john1243",
                "john@gmail.com",
                "99999999",
                "Cleaner with 20 years experience in cleaning bungalows/landed properties",
                LocalTime.of(13,0),
                LocalTime.of(22, 0));

        Worker worker3 = new Worker(
                "Alice Tan",
                "alice",
                "alice1234",
                "alice.tan@example.com",
                "91234567",
                "Experienced cleaner specializing in office spaces and carpet cleaning.",
                LocalTime.of(7, 0),
                LocalTime.of(16, 0)
        );

        Worker worker4 = new Worker(
                "Michael Lee",
                "michael",
                "mike5678",
                "michael.lee@example.com",
                "92345678",
                "Detail-oriented cleaner with expertise in high-rise window cleaning and disinfection.",
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );

        Worker worker5 = new Worker(
                "Sarah Lim",
                "sarah",
                "sarahlim2023",
                "sarah.lim@example.com",
                "93456789",
                "5 years experience in residential cleaning, with a focus on green cleaning methods.",
                LocalTime.of(10, 0),
                LocalTime.of(19, 0)
        );

        Worker worker6 = new Worker(
                "Ravi Kumar",
                "ravi",
                "ravi@999",
                "ravi.kumar@example.com",
                "94567890",
                "Specializes in cleaning kitchens and commercial food preparation areas.",
                LocalTime.of(6, 0),
                LocalTime.of(15, 0)
        );

        Worker worker7 = new Worker(
                "Maria Garcia",
                "maria",
                "maria8765",
                "maria.garcia@example.com",
                "95678901",
                "Friendly and reliable cleaner with 10 years of experience in hotel housekeeping.",
                LocalTime.of(12, 0),
                LocalTime.of(21, 0)
        );

        Worker worker8 = new Worker(
                "Tommy Wu",
                "tommy",
                "tommy0987",
                "tommy.wu@example.com",
                "96789012",
                "Focused on deep-cleaning services for industrial environments.",
                LocalTime.of(14, 0),
                LocalTime.of(23, 0)
        );

        Worker worker9 = new Worker(
                "Lucy Wang",
                "lucy",
                "lucy5432",
                "lucy.wang@example.com",
                "97890123",
                "Residential cleaner with a specialty in eco-friendly products and methods.",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0)
        );

        Worker worker10 = new Worker(
                "David Ong",
                "david",
                "david3210",
                "david.ong@example.com",
                "98901234",
                "Experienced in managing cleaning teams and ensuring high-quality standards in large commercial facilities.",
                LocalTime.of(5, 0),
                LocalTime.of(14, 0)
        );

        workerRepository.saveAll(List.of(worker1, worker2, worker3, worker4, worker5, worker6, worker7, worker8, worker9, worker10));

    }

    @Override
    public void run(String... args) throws Exception {
    }
}
