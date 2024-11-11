package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractReportDto {
    private Long newContracts;
    private Long existingOngoingContracts;
    private Long completedContracts;
}
