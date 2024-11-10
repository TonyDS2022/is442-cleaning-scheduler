package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerHoursDto {
    private Long workerId;
    private Long totalHours;
    private Long overTimeHours;
}
