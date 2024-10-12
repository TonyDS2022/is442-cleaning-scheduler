// temp for retrieving all contracts by cleaningSessionIds
package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import java.util.List;

public class CleaningSessionIdsRequest {
    private List<Integer> cleaningSessionIds;

    // Getters and setters
    public List<Integer> getCleaningSessionIds() {
        return cleaningSessionIds;
    }

    public void setCleaningSessionIds(List<Integer> cleaningSessionIds) {
        this.cleaningSessionIds = cleaningSessionIds;
    }
}