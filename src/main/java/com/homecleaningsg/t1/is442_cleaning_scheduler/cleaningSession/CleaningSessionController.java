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
    public List<CleaningSession> getCleaningSessionsByContractId(@PathVariable int contractId) {
        return cleaningSessionService.getCleaningSessionsByContractId(contractId);
    }

    @GetMapping("/{contractId}/{sessionId}")
    public Optional<CleaningSession> getCleaningSessionByContractIdAndSessionId(@PathVariable int contractId, @PathVariable int sessionId) {
        return cleaningSessionService.getCleaningSessionByContractIdAndSessionId(contractId, sessionId);
    }
}