package com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.*;

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


}