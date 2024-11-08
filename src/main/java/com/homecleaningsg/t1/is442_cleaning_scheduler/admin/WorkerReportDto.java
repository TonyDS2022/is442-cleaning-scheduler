package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerReportDto {
    private Long newWorkers;
    private Long existingWorkers;
    private Long terminatedWorkers;
}
