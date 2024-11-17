package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveBalanceDto {
    private Long workerId;
    private Long annualLeaveBalance;
    private Long medicalLeaveBalance;
}
