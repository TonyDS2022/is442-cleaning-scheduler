package com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Entity
@Table
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {

    @Id
    private String mcId;
    private String blobId;
    private String filename;
    private OffsetDateTime mcStartDate;
    private OffsetDateTime mcEndDate;
    @NonNull
    private Timestamp lastModified;

    public MedicalRecord(String mcId,
                         String blobId,
                         String filename,
                         OffsetDateTime mcStartDate,
                         OffsetDateTime mcEndDate){
        this.mcId = mcId;
        this.blobId = blobId;
        this.filename = filename;
        this.mcStartDate = mcStartDate;
        this.mcEndDate = mcEndDate;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }


}