package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v0.1/cleaningSession")
public class CleaningSessionController {

    private final CleaningSessionService cleaningSessionService;

    @Autowired
    public CleaningSessionController(CleaningSessionService cleaningSessionService) {
        this.cleaningSessionService = cleaningSessionService;
    }

    @PostMapping("/create-cleaning-session/")
    public ResponseEntity<String> addCleaningSession(@RequestBody CleaningSession cleaningSession) {
        try {
            CleaningSession createdSession = cleaningSessionService.addCleaningSession(cleaningSession);
            return ResponseEntity.status(HttpStatus.OK).body("Cleaning session added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add cleaning session.");
        }
    }

    @PutMapping("/update-cleaning-session/{cleaningSessionId}")
    public ResponseEntity<String> updateCleaningSession(
            @PathVariable("cleaningSessionId") Long cleaningSessionId, @RequestBody CleaningSessionUpdateDto updatedSessionDto) {
        try {
            CleaningSession updatedCleaningSession = cleaningSessionService.updateCleaningSession(cleaningSessionId, updatedSessionDto);
            return ResponseEntity.status(HttpStatus.OK).body("Cleaning session updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update cleaning session details.");
        }
    }

    // localhost:8080/api/v0.1/cleaningSession/cancel-cleaning-session/1
    @PutMapping("/cancel-cleaning-session/{cleaningSessionId}")
    public ResponseEntity<String> cancelCleaningSession(@PathVariable("cleaningSessionId") Long cleaningSessionId) {
        try {
            cleaningSessionService.cancelCleaningSession(cleaningSessionId);
            return ResponseEntity.status(HttpStatus.OK).body("Cleaning session cancelled successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to cancel cleaning session.");
        }
    }

    @GetMapping("/calendar-view")
    public List<CleaningSessionCalendarViewDto> getCalendarView() {
        return cleaningSessionService.getCalendarView();
    }

    // localhost:8080/api/v0.1/cleaningSession/calendar-card/57
    @GetMapping("/calendar-card/{cleaningSessionId}")
    public CleaningSessionCalendarCardViewDto getCalendarCardView(
            @PathVariable Long cleaningSessionId) {
        return cleaningSessionService.getCalendarCardView(cleaningSessionId);
    }

}