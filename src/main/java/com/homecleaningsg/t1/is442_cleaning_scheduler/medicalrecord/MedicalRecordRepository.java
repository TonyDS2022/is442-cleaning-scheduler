package com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String> {

    boolean existsByFilename(String filename);
}
