package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v0.1/cleaningSession")
public class CleaningSessionController {

    private final CleaningSessionService cleaningSessionService;

    @Autowired
    public CleaningSessionController(CleaningSessionService cleaningSessionService) {
        this.cleaningSessionService = cleaningSessionService;
    }

    @GetMapping
    public List<CleaningSession> getAllCleaningSessions() {
        return cleaningSessionService.getAllCleaningSessions();
    }

    @GetMapping("/{contractId}")
    public List<CleaningSession> getCleaningSessionsByContractId(@PathVariable Long contractId) {
        return cleaningSessionService.getCleaningSessionsByContractId(contractId);
    }

    @GetMapping("/{contractId}/{cleaningSessionId}")
    public Optional<CleaningSession> getCleaningSessionByContractIdAndCleaningSessionId(@PathVariable Long contractId, @PathVariable Long cleaningSessionId) {
        return cleaningSessionService.getCleaningSessionByContractIdAndCleaningSessionId(contractId, cleaningSessionId);
    }

    @GetMapping("/cleaningSessions/{id}/planningStage")
    // public CleaningSession.PlanningStage getPlanningStage(@PathVariable Long id) {
    //     CleaningSession cleaningSession = cleaningSessionService.getCleaningSessionById(id)
    //             .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));
    //     return cleaningSessionService.getPlanningStage(cleaningSession);
    // }
    public CleaningSession.PlanningStage getPlanningStage(@PathVariable Long id) {
        CleaningSession cleaningSession = cleaningSessionService.getCleaningSessionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));
        return cleaningSession.getPlanningStage();
    }
}