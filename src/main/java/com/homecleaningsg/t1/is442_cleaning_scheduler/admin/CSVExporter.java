package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.WorkerHoursDto;
import com.opencsv.CSVWriter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class CSVExporter {

    public static byte[] exportStatisticsToCsv(
            YearlyStatisticsDto yearlyData,
            List<MonthlyStatisticsDto> monthlyData,
            List<WeeklyStatisticsDto> weeklyData) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos))) {

            // Yearly Data
            writer.writeNext(new String[]{"Yearly Data"});
            writer.writeNext(new String[]{
                    "Year",
                    "TotalNewClients",
                    "TotalExistingClients",
                    "TotalTerminatedClients",
                    "TotalFinishedSessions",
                    "TotalCancelledSessions",
                    "TotalNewContracts",
                    "TotalExistingOngoingContracts",
                    "TotalCompletedContracts",
                    "TotalNewWorkers",
                    "TotalExistingWorkers",
                    "TotalTerminatedWorkers"});

            writer.writeNext(new String[]{
                    String.valueOf(yearlyData.getYear()),
                    String.valueOf(yearlyData.getClientYearlyReport().getNewClients()),
                    String.valueOf(yearlyData.getClientYearlyReport().getExistingClients()),
                    String.valueOf(yearlyData.getClientYearlyReport().getTerminatedClients()),
                    String.valueOf(yearlyData.getSessionYearlyReport().getNumFinishedSessions()),
                    String.valueOf(yearlyData.getSessionYearlyReport().getNumCancelledSessions()),
                    String.valueOf(yearlyData.getContractYearlyReport().getNewContracts()),
                    String.valueOf(yearlyData.getContractYearlyReport().getExistingOngoingContracts()),
                    String.valueOf(yearlyData.getContractYearlyReport().getCompletedContracts()),
                    String.valueOf(yearlyData.getWorkerYearlyReport().getNewWorkers()),
                    String.valueOf(yearlyData.getWorkerYearlyReport().getExistingWorkers()),
                    String.valueOf(yearlyData.getWorkerYearlyReport().getTerminatedWorkers())
            });

            writer.writeNext(new String[]{""});

            // Yearly Worker Hours (Grouped by Worker ID)
            writer.writeNext(new String[]{"Yearly Worker Hours"});
            writer.writeNext(new String[]{"Year", "WorkerId", "TotalHours", "OverTimeHours"});

            for (WorkerHoursDto workerHours : yearlyData.getWorkerYearlyHours()) {
                writer.writeNext(new String[]{
                        String.valueOf(yearlyData.getYear()),
                        String.valueOf(workerHours.getWorkerId()),
                        String.valueOf(workerHours.getTotalHours()),
                        String.valueOf(workerHours.getOverTimeHours())
                });
            }

            writer.writeNext(new String[]{""});

            // Yearly Worker Leave Balance (Grouped by Worker ID)
            writer.writeNext(new String[]{"Yearly Worker Leave Balance"});
            writer.writeNext(new String[]{"Year", "WorkerId", "Annual Leave Balance", "Medical Leave Balance"});

            for (LeaveBalanceDto workerLeaveBalance : yearlyData.getWorkerYearlyLeaveBalance()) {
                writer.writeNext(new String[]{
                        String.valueOf(yearlyData.getYear()),
                        String.valueOf(workerLeaveBalance.getWorkerId()),
                        String.valueOf(workerLeaveBalance.getAnnualLeaveBalance()),
                        String.valueOf(workerLeaveBalance.getMedicalLeaveBalance())
                });
            }

            writer.writeNext(new String[]{""});

            // Monthly Data
            writer.writeNext(new String[]{"Monthly Data"});
            writer.writeNext(new String[]{
                    "Year",
                    "Month",
                    "TotalNewClients",
                    "TotalExistingClients",
                    "TotalTerminatedClients",
                    "TotalFinishedSessions",
                    "TotalCancelledSessions",
                    "TotalNewContracts",
                    "TotalExistingOngoingContracts",
                    "TotalCompletedContracts",
                    "TotalNewWorkers",
                    "TotalExistingWorkers",
                    "TotalTerminatedWorkers"});

            for (MonthlyStatisticsDto monthlyStat : monthlyData) {
                writer.writeNext(new String[]{
                        String.valueOf(monthlyStat.getYear()),
                        String.valueOf(monthlyStat.getMonth()),
                        String.valueOf(monthlyStat.getClientMonthlyReport().getNewClients()),
                        String.valueOf(monthlyStat.getClientMonthlyReport().getExistingClients()),
                        String.valueOf(monthlyStat.getClientMonthlyReport().getTerminatedClients()),
                        String.valueOf(monthlyStat.getSessionMonthlyReport().getNumFinishedSessions()),
                        String.valueOf(monthlyStat.getSessionMonthlyReport().getNumCancelledSessions()),
                        String.valueOf(monthlyStat.getContractMonthlyReport().getNewContracts()),
                        String.valueOf(monthlyStat.getContractMonthlyReport().getExistingOngoingContracts()),
                        String.valueOf(monthlyStat.getContractMonthlyReport().getCompletedContracts()),
                        String.valueOf(monthlyStat.getWorkerMonthlyReport().getNewWorkers()),
                        String.valueOf(monthlyStat.getWorkerMonthlyReport().getExistingWorkers()),
                        String.valueOf(monthlyStat.getWorkerMonthlyReport().getTerminatedWorkers())
                });
            }

            writer.writeNext(new String[]{""});

            // Monthly Worker Hours (Grouped by Worker ID)
            writer.writeNext(new String[]{"Monthly Worker Hours"});
            writer.writeNext(new String[]{"Year", "Month", "WorkerId", "TotalHours", "OverTimeHours"});

            for (MonthlyStatisticsDto monthlyStat : monthlyData) {
                for (WorkerHoursDto workerHours : monthlyStat.getWorkerMonthlyHours()) {
                    writer.writeNext(new String[]{
                            String.valueOf(monthlyStat.getYear()),
                            String.valueOf(monthlyStat.getMonth()),
                            String.valueOf(workerHours.getWorkerId()),
                            String.valueOf(workerHours.getTotalHours()),
                            String.valueOf(workerHours.getOverTimeHours())
                    });
                }
            }

            writer.writeNext(new String[]{""});

            // Monthly Worker Leaves Taken (Grouped by Worker ID)
            writer.writeNext(new String[]{"Monthly Worker Leave Taken"});
            writer.writeNext(new String[]{"Year", "Month", "WorkerId", "Annual Leave Taken", "Medical Leave Taken"});

            for (MonthlyStatisticsDto monthlyStat : monthlyData) {
                for (LeaveTakenDto workerLeaveTaken : monthlyStat.getWorkerMonthlyLeaveTaken()) {
                    writer.writeNext(new String[]{
                            String.valueOf(monthlyStat.getYear()),
                            String.valueOf(monthlyStat.getMonth()),
                            String.valueOf(workerLeaveTaken.getWorkerId()),
                            String.valueOf(workerLeaveTaken.getAnnualLeaveTaken()),
                            String.valueOf(workerLeaveTaken.getMedicalLeaveTaken())
                    });
                }
            }

            writer.writeNext(new String[]{""});

            // Weekly Worker Hours (Grouped by Worker ID)
            writer.writeNext(new String[]{"Weekly Worker Hours"});
            writer.writeNext(new String[]{"Year", "WeekStart", "WeekEnd", "WorkerId", "TotalHours", "OverTimeHours"});

            for (WeeklyStatisticsDto weeklyStat : weeklyData) {
                for (WorkerHoursDto workerHours : weeklyStat.getWorkerWeeklyHours()) {
                    writer.writeNext(new String[]{
                            String.valueOf(weeklyStat.getStartOfWeek().getYear()),
                            weeklyStat.getStartOfWeek().toString(),
                            weeklyStat.getEndOfWeek().toString(),
                            String.valueOf(workerHours.getWorkerId()),
                            String.valueOf(workerHours.getTotalHours()),
                            String.valueOf(workerHours.getOverTimeHours())
                    });
                }
            }

            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV file", e);
        }
    }
}
