package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsReportDto {
    private int year;
    private YearlyStatisticsDto yearlyStats;
    private List<MonthlyStatisticsDto> monthlyStats;
    private List<WeeklyStatisticsDto> weeklyStats;
}
