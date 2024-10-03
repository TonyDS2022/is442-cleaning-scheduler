package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CleaningSessionId implements Serializable {
    private int contract; // This should match the type of the primary key in Contract
    private int sessionId;
}