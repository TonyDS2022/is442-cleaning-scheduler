package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/create-cleaning-session/")
    public ResponseEntity<CleaningSession> createCleaningSession(@RequestBody CleaningSession cleaningSession) {
        try {
            CleaningSession createdSession = cleaningSessionService.createCleaningSession(cleaningSession);
            return ResponseEntity.ok(createdSession);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update-cleaning-session/{id}")
    public ResponseEntity<CleaningSession> updateCleaningSession(
            @PathVariable Long cleaningSessionId, @RequestBody CleaningSession updatedSession) {
        try {
            CleaningSession updatedCleaningSession = cleaningSessionService.updateCleaningSession(cleaningSessionId, updatedSession);
            return ResponseEntity.ok(updatedCleaningSession);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete-cleaning-session/{id}")
    public ResponseEntity<Void> deleteCleaningSession(@PathVariable Long cleaningSessionId) {
        try {
            cleaningSessionService.deleteCleaningSession(cleaningSessionId);
            return ResponseEntity.noContent().build(); // Return 204 No Content if deletion is successful
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if the session doesn't exist
        }
    }
}