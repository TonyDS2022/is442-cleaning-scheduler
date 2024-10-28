package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ShiftWorkerService shiftWorkerService;
    private final LeaveApplicationService leaveApplicationService;

    @Autowired
    public ShiftController(ShiftService shiftService,
                           ShiftWorkerService shiftWorkerService,
                           LeaveApplicationService leaveApplicationService) {
        this.shiftService = shiftService;
        this.shiftWorkerService = shiftWorkerService;
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

    @PostMapping
    public void addShift(@RequestBody Shift shift) {
        shiftService.addShift(shift);
    }

    @PutMapping("/{shiftId}")
    public void updateShift(@PathVariable("shiftId") Long shiftId, @RequestBody Shift shift) {
        shiftService.updateShift(shiftId, shift);
    }

    @DeleteMapping("/{shiftId}")
    public void deleteShift(@PathVariable("shiftId") Long shiftId) {
        shiftService.deleteShift(shiftId);
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
    @GetMapping("/worker/{workerId}/last-known-location")
    public Location getWorkerLastKnownLocation(@PathVariable("workerId") Long workerId,
                                               @RequestParam LocalDate date,
                                               @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time) {

        return shiftWorkerService.getWorkerLastKnownLocation(workerId, date, time);
    }

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

}