package com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {

    @Id
    private String mcId;
    private String blobId;
    private String filename;
    private LocalDate mcStartDate;
    private LocalDate mcEndDate;


}