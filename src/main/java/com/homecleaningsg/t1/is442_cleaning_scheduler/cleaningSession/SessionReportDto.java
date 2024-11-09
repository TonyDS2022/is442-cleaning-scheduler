package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionReportDto {
    private Long noSessions;
    private Long noCancelledSessions;
}
