package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerReportDto {
    private Long newWorkers;
    private Long existingWorkers;
    private Long terminatedWorkers;
}
