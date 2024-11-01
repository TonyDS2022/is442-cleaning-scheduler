package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v0.1/shift")
public class ShiftController {

    private final ShiftService shiftService;
    private final LeaveApplicationService leaveApplicationService;

    @Autowired
    public ShiftController(ShiftService shiftService,
                           LeaveApplicationService leaveApplicationService) {
        this.shiftService = shiftService;
        this.leaveApplicationService = leaveApplicationService;
    }

    @GetMapping
    public List<Shift> getAllShifts() {
        return shiftService.getAllShifts();
    }

    @GetMapping("/{shiftId}")
    public Optional<Shift> getShiftById(@PathVariable("shiftId") Long shiftId) {
        return shiftService.getShiftById(shiftId);
    }

    @PostMapping("/add-shift/")
    public ResponseEntity<String> addShift(@RequestBody Shift shift) {
        try{
            shiftService.addShift(shift);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Shift added successfully.");
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add shift.");
        }
    }

    // localhost:8080/api/v0.1/shift/update-shift/1
    @PutMapping("/update-shift/{shiftId}")
    public ResponseEntity<String> updateShift(
            @PathVariable("shiftId") Long shiftId, @RequestBody Shift updatedShift) {
        try{
            shiftService.updateShift(shiftId, updatedShift);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Shift updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update shift details.");
        }
    }

    // localhost:8080/api/v0.1/shift/deactivate-shift/2
    @PutMapping("/deactivate-shift/{shiftId}")
    public ResponseEntity<String> deactivateShift(@PathVariable("shiftId") Long shiftId) {
        try{
            shiftService.deactivateShift(shiftId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("The shift has been successfully deactivated, and associated workers removed from shift.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to deactivate shift.");
        }
    }

    @GetMapping("/worker/{workerId}")
    public List<Shift> getShiftsByWorkerId(@PathVariable("workerId") Long workerId) {
        return shiftService.getShiftsByWorkerId(workerId);
    }

    // get shifts by worker and month / week / day
    @GetMapping("/worker/{workerId}/month")
    public List<Shift> getShiftsByMonthAndWorker(@PathVariable("workerId") Long workerId, @RequestParam int month, @RequestParam int year) {
        return shiftService.getShiftsByMonthAndWorker(month, year, workerId);
    }

    // http://localhost:8080/api/v0.1/shift/worker/1/week=40&year=2024
    @GetMapping("/worker/{workerId}/week")
    public List<Shift> getShiftsByWeekAndWorker(@PathVariable("workerId") Long workerId, @RequestParam int week, @RequestParam int year) {
        return shiftService.getShiftsByWeekAndWorker(week, year, workerId);
    }

    // http://localhost:8080/api/v0.1/shift/worker/1/day?date=2024-10-05
    @GetMapping("/worker/{workerId}/day")
    public List<Shift> getShiftsByDayAndWorker(@PathVariable("workerId") Long workerId, @RequestParam LocalDate date) {
        return shiftService.getShiftsByDayAndWorker(date, workerId);
    }

    // http://localhost:8080/api/v0.1/shift/worker/1/dayLastShiftBeforeTime?date=2024-10-05&time=15:00
    @GetMapping("/worker/{workerId}/dayLastShiftBeforeTime")
    public List<Shift> getLastShiftByDayAndWorkerBeforeTime(@PathVariable("workerId") Long workerId,
                                                                      @RequestParam LocalDate date,
                                                                      @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time) {

        return shiftService.getLastShiftByDayAndWorkerBeforeTime(workerId, date, time);
    }

    @PostMapping("/{shiftId}/set-worker/{workerId}")
    public ResponseEntity<String> assignWorkerToShift(@PathVariable("shiftId") Long shiftId, @PathVariable("workerId") Long workerId) {
        try {
            shiftService.setWorker(shiftId, workerId);
            return ResponseEntity.ok("Worker successfully assigned to shift.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // http://localhost:8080/api/v0.1/shift/worker/1/last-known-location?date=2024-10-05&time=15:00
//    @GetMapping("/worker/{workerId}/last-known-location")
//    public Location getWorkerLastKnownLocation(@PathVariable("workerId") Long workerId,
//                                               @RequestParam LocalDate date,
//                                               @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time) {
//
//        return shiftWorkerService.getWorkerLastKnownLocation(workerId, date, time);
//    }

    // http://localhost:8080/api/v0.1/shift/worker/5/is-worker-on-leave?shiftStartDate=2024-10-05&shiftStartTime=15:00&shiftEndDate=2024-10-05&shiftEndTime=18:00
    @GetMapping("/worker/{workerId}/is-worker-on-leave")
    public ResponseEntity<Boolean> isWorkerOnLeave(
            @PathVariable Long workerId,
            @RequestParam LocalDate shiftStartDate,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime shiftStartTime,
            @RequestParam LocalDate shiftEndDate,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime shiftEndTime) {
        boolean isOnLeave = leaveApplicationService.isWorkerOnLeave(workerId, shiftStartDate, shiftStartTime, shiftEndDate, shiftEndTime);
        return ResponseEntity.ok(isOnLeave);
    }

    @GetMapping("/{shiftId}/available-workers")
    public List<AvailableWorkerDto> getAvailableWorkers(@PathVariable("shiftId") Long shiftId) {
        return shiftService.getAvailableWorkersForShift(shiftId);
    }

}