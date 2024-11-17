package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
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
    public ResponseEntity<?> getShiftById(@PathVariable("shiftId") Long shiftId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(shiftService.getShiftById(shiftId));
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
    public ResponseEntity<?> getShiftsByWorkerId(@PathVariable("workerId") Long workerId) {
        try {
            List<ShiftWithWorkerDetailsDto> shifts = shiftService.getShiftsDtosByWorkerId(workerId);
            return ResponseEntity.ok(shifts);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/worker/{workerId}/month")
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