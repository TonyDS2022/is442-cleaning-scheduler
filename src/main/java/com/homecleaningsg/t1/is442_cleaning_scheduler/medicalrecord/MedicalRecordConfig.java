package com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;

@Configuration
public class MedicalRecordConfig {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordConfig(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @Bean
    CommandLineRunner medicalRecordCommandLineRunner(MedicalRecordRepository medicalRecordRepository) {
        return args -> {
            // Generating custom mcIds using the service
            String mcId1 = medicalRecordService.generateCustomMcId();
            String mcId2 = medicalRecordService.generateCustomMcId();
            String mcId3 = medicalRecordService.generateCustomMcId();

            // Creating test data with fake blobId and dates
            MedicalRecord medicalRecord1 = MedicalRecord.builder()
                    .mcId(mcId1)
                    .blobId("fake-blob-123")
                    .mcStartDate(OffsetDateTime.now())
                    .mcEndDate(OffsetDateTime.now().plusDays(7))
                    .build();

            MedicalRecord medicalRecord2 = MedicalRecord.builder()
                    .mcId(mcId2)
                    .blobId("fake-blob-456")
                    .mcStartDate(OffsetDateTime.now().minusDays(10))
                    .mcEndDate(OffsetDateTime.now().minusDays(3))
                    .build();

            MedicalRecord medicalRecord3 = MedicalRecord.builder()
                    .mcId(mcId3)
                    .blobId("fake-blob-789")
                    .mcStartDate(OffsetDateTime.now().plusDays(5))
                    .mcEndDate(OffsetDateTime.now().plusDays(12))
                    .build();

            // Save the test data to the repository (and hence to the database)
            medicalRecordRepository.save(medicalRecord1);
            medicalRecordRepository.save(medicalRecord2);
            medicalRecordRepository.save(medicalRecord3);
        };
    }
}
