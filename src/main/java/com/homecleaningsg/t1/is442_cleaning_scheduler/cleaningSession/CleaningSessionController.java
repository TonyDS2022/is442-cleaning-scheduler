package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Retrieve the calendar view of cleaning sessions",
            description = "Fetches a list of cleaning sessions in a calendar view format.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the calendar view.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CleaningSessionCalendarViewDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to retrieve the calendar view due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Unable to retrieve calendar view.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getCalendarView() {
        try {
            List<CleaningSessionCalendarViewDto> calendarView = cleaningSessionService.getCalendarView();
            return ResponseEntity.status(HttpStatus.OK).body(calendarView);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to retrieve calendar view.");
        }
    }

    // localhost:8080/api/v0.1/cleaningSession/calendar-card/57
    @GetMapping("/calendar-card/{cleaningSessionId}")
    public ResponseEntity<?> getCalendarCard(@PathVariable("cleaningSessionId") Long cleaningSessionId) {
        try {
            CleaningSessionCalendarCardViewDto calendarCard = cleaningSessionService.getCalendarCardView(cleaningSessionId);
            return ResponseEntity.status(HttpStatus.OK).body(calendarCard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to retrieve calendar card.");
        }
    }

}