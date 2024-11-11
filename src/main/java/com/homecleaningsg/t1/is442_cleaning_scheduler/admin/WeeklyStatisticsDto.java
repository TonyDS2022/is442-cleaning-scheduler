package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.WorkerHoursDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyStatisticsDto {
    private LocalDate startOfWeek;
    private LocalDate endOfWeek;
    private List<WorkerHoursDto> workerWeeklyHours; //list of hours per worker
}
