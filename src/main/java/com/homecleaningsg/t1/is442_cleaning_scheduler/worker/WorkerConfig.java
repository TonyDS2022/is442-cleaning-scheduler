package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;


import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class WorkerConfig implements CommandLineRunner{

    private final WorkerRepository workerRepository;
    private final LocationRepository locationRepository;

    public WorkerConfig(WorkerRepository workerRepository, LocationRepository locationRepository) {
        this.workerRepository = workerRepository;
        this.locationRepository = locationRepository;

        Worker worker1 = new Worker(
                "Karthiga",
                "karthiga",
                "kar1243",
                "karthigamagesh17@gmail.com",
                "99999999",
                "Hello, I am a sincere cleaner specialised in wooden floors",
                LocalTime.of(8,0),
                LocalTime.of(17,0));
        Location loc1 = locationRepository.findByPostalCode("649823")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker1.setHomeLocation(loc1);

        Worker worker2 = new Worker(
                "John",
                "john",
                "john1243",
                "john@gmail.com",
                "99999999",
                "Cleaner with 20 years experience in cleaning bungalows/landed properties",
                LocalTime.of(13,0),
                LocalTime.of(22, 0));
        Location loc2 = locationRepository.findByPostalCode("438181")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker2.setHomeLocation(loc2);

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
        Location loc3 = locationRepository.findByPostalCode("238830")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker3.setHomeLocation(loc3);

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
        Location loc4 = locationRepository.findByPostalCode("049213")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker4.setHomeLocation(loc4);

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

        Location loc5 = locationRepository.findByPostalCode("018956")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker5.setHomeLocation(loc5);

        Worker worker6 = new Worker(
                "Ravi Kumar",
                "ravi",
                "ravi@999",
                "ravi.kumar@example.com",
                "94567890",
                "Specializes in cleaning kitchens and commercial food preparation areas.",
                LocalTime.of(9, 0),
                LocalTime.of(15, 0)
        );
        Location loc6 = locationRepository.findByPostalCode("039803")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker6.setHomeLocation(loc6);

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
        Location loc7 = locationRepository.findByPostalCode("729826")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker7.setHomeLocation(loc7);

        Worker worker8 = new Worker(
                "Tommy Wu",
                "tommy",
                "tommy0987",
                "tommy.wu@example.com",
                "96789012",
                "Focused on deep-cleaning services for industrial environments.",
                LocalTime.of(14, 0),
                LocalTime.of(22, 0)
        );
        Location loc8 = locationRepository.findByPostalCode("018953")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker8.setHomeLocation(loc8);

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
        Location loc9 = locationRepository.findByPostalCode("039594")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker9.setHomeLocation(loc9);

        Worker worker10 = new Worker(
                "David Ong",
                "david",
                "david3210",
                "david.ong@example.com",
                "98901234",
                "Experienced in managing cleaning teams and ensuring high-quality standards in large commercial facilities.",
                LocalTime.of(8, 0),
                LocalTime.of(14, 0)
        );
        Location loc10 = locationRepository.findByPostalCode("098269")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker10.setHomeLocation(loc10);

        Worker worker11 = new Worker(
                "Stephen Hawking",
                "Einstein",
                "blackhole",
                "hawking@gmail.com",
                "99999999",
                "Hello, I am a sincere cleaner specialised in wooden floors",
                LocalTime.of(8,0),
                LocalTime.of(17,0));
        Location loc11 = locationRepository.findByPostalCode("259569")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker11.setHomeLocation(loc11);

        Worker worker12 = new Worker(
                "Alan Turing",
                "turing",
                "124321",
                "alanturns@gmail.com",
                "99999999",
                "I've had to help my parents since I was 14, my role was to carry a 20 kg bucket, keep giving the machine clean water, and throw dirty water in the restroom. When we finished working, my father paid me 1 USD per hour.",
                LocalTime.of(8,0),
                LocalTime.of(17,0));
        Location loc12 = locationRepository.findByPostalCode("238801")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker12.setHomeLocation(loc12);

        Worker worker13 = new Worker(
                "Ada Lovelace",
                "ada",
                "ada123",
                "livelove@hotmail.com",
                "99999999",
                "I have experience in cleaning houses.I also do massages for women.",
                LocalTime.of(13,0),
                LocalTime.of(22, 0));
        Location loc13 = locationRepository.findByPostalCode("189673")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker13.setHomeLocation(loc13);

        Worker worker14 = new Worker(
                "Grace Hopper",
                "grace",
                "grace123",
                "gracethyearth@gmail.com",
                "99999999",
                "House cleaning is an intimate encounter that demands respect from the cleaner. I felt that my position was privileged.",
                LocalTime.of(7, 0),
                LocalTime.of(16, 0));
        Location loc14 = locationRepository.findByPostalCode("038981")
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        worker14.setHomeLocation(loc14);

        workerRepository.saveAll(List.of(worker1, worker2, worker3, worker4, worker5,
                worker6, worker7, worker8, worker9, worker10, worker11, worker12, worker13, worker14));
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
