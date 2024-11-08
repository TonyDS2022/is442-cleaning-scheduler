package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientReportDto {
    private Long newClients;
    private Long existingClients;
    private Long terminatedClients;
}
