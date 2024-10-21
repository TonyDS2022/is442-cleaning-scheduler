package com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class MedicalRecordConfig implements CommandLineRunner{

    private final MedicalRecordService medicalRecordService;
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordConfig(MedicalRecordRepository medicalRecordRepository, MedicalRecordService medicalRecordService) {
        this.medicalRecordRepository =medicalRecordRepository;
        this.medicalRecordService = medicalRecordService;
    }

    @Override
    public void run(String... args) throws Exception {
        String mcId1 = medicalRecordService.generateCustomMcId();
        String mcId2 = medicalRecordService.generateCustomMcId();
        String mcId3 = medicalRecordService.generateCustomMcId();

        MedicalRecord medicalRecord1 = new MedicalRecord(mcId1, "fake-blob-123", "user1_image1_timestamp", OffsetDateTime.now(), OffsetDateTime.now().plusDays(7));
        MedicalRecord medicalRecord2 = new MedicalRecord(mcId2, "fake-blob-456", "user1_image2_timestamp", OffsetDateTime.now().minusDays(10), OffsetDateTime.now().minusDays(3));

        medicalRecordRepository.saveAll(List.of(medicalRecord1, medicalRecord2));
    }

}