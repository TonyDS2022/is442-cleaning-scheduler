package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v0.1/shift")
public class ShiftController {

    private final ShiftService shiftService;

    @Autowired
    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
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

    @PostMapping("/{shiftId}/set-worker/{workerId}")
    public ResponseEntity<String> assignWorkerToShift(@PathVariable("shiftId") Long shiftId, @PathVariable("workerId") Long workerId) {
        try {
            shiftService.setWorker(shiftId, workerId);
            return ResponseEntity.ok("Worker successfully assigned to shift.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}