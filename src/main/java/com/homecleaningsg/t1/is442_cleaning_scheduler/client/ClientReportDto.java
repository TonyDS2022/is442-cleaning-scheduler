package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientReportDto {
    private Long newClients;
    private Long existingClients;
    private Long terminatedClients;
}
