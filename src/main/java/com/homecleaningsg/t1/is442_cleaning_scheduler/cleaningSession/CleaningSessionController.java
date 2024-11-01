package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/create-cleaning-session/")
    public ResponseEntity<String> addCleaningSession(@RequestBody CleaningSession cleaningSession) {
        try {
            CleaningSession createdSession = cleaningSessionService.addCleaningSession(cleaningSession);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Cleaning session added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add cleaning session.");
        }
    }

    @PutMapping("/update-cleaning-session/{cleaningSessionId}")
    public ResponseEntity<String> updateCleaningSession(
            @PathVariable("cleaningSessionId") Long cleaningSessionId, @RequestBody CleaningSessionUpdateDto updatedSessionDto) {
        try {
            CleaningSession updatedCleaningSession = cleaningSessionService.updateCleaningSession(cleaningSessionId, updatedSessionDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Cleaning session updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update cleaning session details.");
        }
    }

    // localhost:8080/api/v0.1/cleaningSession/deactivate-cleaning-session/1
    @PutMapping("/deactivate-cleaning-session/{cleaningSessionId}")
    public ResponseEntity<String> deactivateCleaningSession(@PathVariable("cleaningSessionId") Long cleaningSessionId) {
        try {
            cleaningSessionService.deactivateCleaningSession(cleaningSessionId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Cleaning session deactivated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to deactivate cleaning session.");
        }
    }

    @GetMapping("/cleaningSessionId/{id}")
    public CleaningSession getPlanningStage(@PathVariable Long id) {
        return cleaningSessionService.getCleaningSessionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));
    }

}