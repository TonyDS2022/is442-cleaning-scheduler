package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CleaningSessionUpdateDto {
    private int workersBudgeted;
    private LocalDate sessionStartDate;
    private LocalTime sessionStartTime;
    private LocalDate sessionEndDate;
    private LocalTime sessionEndTime;
    private String sessionDescription;
    private CleaningSession.Rating sessionRating;
    private CleaningSession.PlanningStage planningStage;
    private String sessionFeedback;
}
