package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AvailableWorkerDto {
    private Long workerId;
    private String workerName;
    private String workerLastLocation;
    private String destination;
    private double tripDurationSeconds;
    private double tripDistanceMeters;
}
