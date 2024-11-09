package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.SessionReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.WorkerHoursDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerReportDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyStatisticsDto {
    private int year;
    private int month;
    private List<WorkerHoursDto> workerMonthlyHours; //list of hours per worker
    private ClientReportDto clientMonthlyReport;
    private SessionReportDto sessionMonthlyReport;
    private ContractReportDto contractMonthlyReport;
    private WorkerReportDto workerMonthlyReport;
}
