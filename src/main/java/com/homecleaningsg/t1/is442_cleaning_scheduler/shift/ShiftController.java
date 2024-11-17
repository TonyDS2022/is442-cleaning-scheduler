package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v0.1/shift")
public class ShiftController {

    private final ShiftService shiftService;
    private final LeaveApplicationService leaveApplicationService;
    private final WorkerService workerService;

    @Autowired
    public ShiftController(ShiftService shiftService,
                           LeaveApplicationService leaveApplicationService, WorkerService workerService) {
        this.shiftService = shiftService;
        this.leaveApplicationService = leaveApplicationService;
        this.workerService = workerService;
    }

    @GetMapping("/{shiftId}")
    @Operation(
            summary = "Retrieve shift details by ID",
            description = "Fetches the details of a specific shift, including worker details and trip information, using the shift ID.",
            parameters = {
                    @Parameter(
                            name = "shiftId",
                            description = "The ID of the shift to retrieve",
                            required = true,
                            example = "123"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the shift details.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ShiftWithWorkerDetailAndTripDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to retrieve the shift due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Invalid shift ID provided.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getShiftById(@PathVariable("shiftId") Long shiftId) {
        try {
            ShiftWithWorkerDetailAndTripDto shift = shiftService.getShiftById(shiftId);
            return ResponseEntity.status(HttpStatus.OK).body(shift);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // localhost:8080/api/v0.1/shift/update-shift/1
    @PutMapping("/update-shift/{shiftId}")
    public ResponseEntity<String> updateShift(
            @PathVariable("shiftId") Long shiftId,
            @RequestBody Map<String, String> updates
    ) {
        try{
            shiftService.updateShift(shiftId, updates);
            return ResponseEntity.status(HttpStatus.OK).body("Shift updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // localhost:8080/api/v0.1/shift/unassign-worker/1
    @PutMapping("/unassign-worker/{shiftId}")
    public ResponseEntity<String> unassignWorker(
            @PathVariable("shiftId") Long shiftId
    ) {
        try{
            shiftService.unassignWorkerFromShift(shiftId);
            return ResponseEntity.status(HttpStatus.OK).body("Worker unassigned from shift successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // localhost:8080/api/v0.1/shift/start-shift/1
    @PutMapping("/start-shift/{shiftId}")
    public ResponseEntity<String> startShift(
            @PathVariable Long shiftId
    ) {
        try{
            shiftService.startShift(shiftId);
            return ResponseEntity.status(HttpStatus.OK).body("Shift started successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to start shift.");
        }
    }

    // localhost:8080/api/v0.1/shift/end-shift/1
    @PutMapping("/end-shift/{shiftId}")
    public ResponseEntity<String> endShift(
            @PathVariable Long shiftId
    ) {
        try{
            shiftService.endShift(shiftId);
            return ResponseEntity.status(HttpStatus.OK).body("Shift ended successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to end shift. "  + e.getMessage());
        }
    }

    // localhost:8080/api/v0.1/shift/cancel-shift/2
    @PutMapping("/cancel-shift/{shiftId}")
    public ResponseEntity<String> cancelShift(@PathVariable("shiftId") Long shiftId) {
        try{
            shiftService.cancelShift(shiftId);
            return ResponseEntity.status(HttpStatus.OK).body("The shift has been successfully cancelled, and associated workers removed from shift.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to cancel shift.");
        }
    }

    @GetMapping("/worker/{workerId}")
    @Operation(
            summary = "Retrieve shifts by worker ID",
            description = "Fetches a list of shifts assigned to a worker, including worker details and trip information, using the worker ID.",
            parameters = {
                    @Parameter(
                            name = "workerId",
                            description = "The ID of the worker to retrieve shifts for",
                            required = true,
                            example = "123"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the shifts assigned to the worker.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ShiftWithWorkerDetailAndTripDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to retrieve the shifts due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Invalid worker ID provided.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getShiftsByWorkerId(@PathVariable("workerId") Long workerId) {
        try {
            List<ShiftWithWorkerDetailsDto> shifts = shiftService.getShiftsDtosByWorkerId(workerId);
            return ResponseEntity.ok(shifts);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/worker/{workerId}/month")
    @Operation(
            summary = "Retrieve shifts by month and worker ID",
            description = "Fetches a list of shifts assigned to a worker in a specific month, including worker details and trip information, using the worker ID.",
            parameters = {
                    @Parameter(
                            name = "workerId",
                            description = "The ID of the worker to retrieve shifts for",
                            required = true,
                            example = "123"
                    ),
                    @Parameter(
                            name = "month",
                            description = "The month to retrieve shifts for",
                            required = true,
                            example = "1"
                    ),
                    @Parameter(
                            name = "year",
                            description = "The year to retrieve shifts for",
                            required = true,
                            example = "2022"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the shifts assigned to the worker in the specified month.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ShiftWithWorkerDetailsDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to retrieve the shifts due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Invalid worker ID provided.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getShiftsByMonthAndWorker(@PathVariable("workerId") Long workerId,
                                                       @RequestParam int month,
                                                       @RequestParam int year) {
        try {
            List<ShiftWithWorkerDetailsDto> shifts = shiftService.getShiftsByMonthAndWorker(month, year, workerId);
            return ResponseEntity.ok(shifts);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/worker/{workerId}/week")
    @Operation(
            summary = "Retrieve shifts by week and worker ID",
            description = "Fetches a list of shifts assigned to a worker in a specific week, including worker details and trip information, using the worker ID.",
            parameters = {
                    @Parameter(
                            name = "workerId",
                            description = "The ID of the worker to retrieve shifts for",
                            required = true,
                            example = "123"
                    ),
                    @Parameter(
                            name = "week",
                            description = "The week to retrieve shifts for",
                            required = true,
                            example = "1"
                    ),
                    @Parameter(
                            name = "year",
                            description = "The year to retrieve shifts for",
                            required = true,
                            example = "2022"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the shifts assigned to the worker in the specified week.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ShiftWithWorkerDetailsDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to retrieve the shifts due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Invalid worker ID provided.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getShiftsByWeekAndWorker(@PathVariable("workerId") Long workerId,
                                                      @RequestParam int week,
                                                      @RequestParam int year) {
        try {
            List<ShiftWithWorkerDetailsDto> shifts = shiftService.getShiftsByWeekAndWorker(week, year, workerId);
            return ResponseEntity.ok(shifts);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/worker/{workerId}/day")
    @Operation(
            summary = "Retrieve shifts by day and worker ID",
            description = "Fetches a list of shifts assigned to a worker on a specific day, including worker details and trip information, using the worker ID.",
            parameters = {
                    @Parameter(
                            name = "workerId",
                            description = "The ID of the worker to retrieve shifts for",
                            required = true,
                            example = "123"
                    ),
                    @Parameter(
                            name = "date",
                            description = "The date to retrieve shifts for",
                            required = true,
                            example = "2022-01-01"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the shifts assigned to the worker on the specified day.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ShiftWithWorkerDetailsDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to retrieve the shifts due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Invalid worker ID provided.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getShiftsByDayAndWorker(@PathVariable("workerId") Long workerId,
                                                     @RequestParam LocalDate date) {
        try {
            List<ShiftWithWorkerDetailsDto> shifts = shiftService.getShiftsByDayAndWorker(date, workerId);
            return ResponseEntity.ok(shifts);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/worker/{workerId}/dayLastShiftBeforeTime")
    @Operation(
            summary = "Retrieve the last shift by day and worker ID before a specific time",
            description = "Fetches the last shift assigned to a worker on a specific day before a specific time, including worker details and trip information, using the worker ID.",
            parameters = {
                    @Parameter(
                            name = "workerId",
                            description = "The ID of the worker to retrieve shifts for",
                            required = true,
                            example = "123"
                    ),
                    @Parameter(
                            name = "date",
                            description = "The date to retrieve shifts for",
                            required = true,
                            example = "2022-01-01"
                    ),
                    @Parameter(
                            name = "time",
                            description = "The time to retrieve shifts before",
                            required = true,
                            example = "12:00"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the last shift assigned to the worker on the specified day before the specified time.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ShiftWithWorkerDetailsDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to retrieve the shifts due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Invalid worker ID provided.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getLastShiftByDayAndWorkerBeforeTime(@PathVariable("workerId") Long workerId,
                                                                  @RequestParam LocalDate date,
                                                                  @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time) {
        try {
            List<ShiftWithWorkerDetailsDto> shifts = shiftService.getLastShiftByDayAndWorkerBeforeTime(workerId, date, time);
            return ResponseEntity.ok(shifts);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/{shiftId}/assign-worker/{workerId}")
    public ResponseEntity<String> assignWorkerToShift(@PathVariable("shiftId") Long shiftId, @PathVariable("workerId") Long workerId) {
        try {
            shiftService.setWorker(shiftId, workerId);
            return ResponseEntity.ok("Worker successfully assigned to shift.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{shiftId}/available-workers")
    @Operation(
            summary = "Retrieve available workers for a shift",
            description = "Fetches a list of workers who are available for a specific shift, based on their leave applications and existing shift assignments. The list includes worker details and trip information and is returned in ascending order of tripDurationSeconds. If a worker did not have a preceding shift, they are deemed to be travelling from their home location. Otherwise, they are deemed to be travelling from their preceding shift client location.",
            parameters = {
                    @Parameter(
                            name = "shiftId",
                            description = "The ID of the shift to retrieve available workers for",
                            required = true,
                            example = "123"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the available workers for the shift.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AvailableWorkerDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to retrieve the available workers due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Invalid shift ID provided.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getAvailableWorkers(@PathVariable("shiftId") Long shiftId) {
        try {
            List<AvailableWorkerDto> availableWorkers = shiftService.getAvailableWorkersForShift(shiftId);
            return ResponseEntity.ok(availableWorkers);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    // DTO for getting dynamic workerhaspendingleave status
    // given a workerId, find all leave applications in leaveApplicationRepo
    // that are pending
    // that have matching workerid
    // and whos leave have a start and end date that overlaps with the cleaning session
    @GetMapping("/worker/{workerId}/shifts-with-pending-leave")
    @Operation(
            summary = "Retrieve shifts with pending leave for a worker",
            description = "Fetches a list of shifts assigned to a worker, along with a boolean flag indicating whether the worker has pending or approved leave applications that overlap with the shift dates.",
            parameters = {
                    @Parameter(
                            name = "workerId",
                            description = "The ID of the worker to retrieve shifts for",
                            required = true,
                            example = "123"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the shifts assigned to the worker, along with the pending leave status.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = WorkerPendingLeaveDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Unable to retrieve the shifts due to invalid input or other errors.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(type = "string", example = "Invalid worker ID provided.")
                            )
                    )
            }
    )
    public ResponseEntity<?> getShiftsWithPendingLeave(@PathVariable("workerId") Long workerId) {
        try {
            List<Shift> shifts = shiftService.getShiftsByWorkerId(workerId);
            List<WorkerPendingLeaveDto> result = shifts.stream()
                    .map(shift -> {
                        boolean hasPendingLeave = workerService.workerHasPendingOrApprovedLeaveBetween(
                                workerId,
                                shift.getSessionStartDate(),
                                shift.getSessionEndDate()
                        );
                        return new WorkerPendingLeaveDto(shift, hasPendingLeave);
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}