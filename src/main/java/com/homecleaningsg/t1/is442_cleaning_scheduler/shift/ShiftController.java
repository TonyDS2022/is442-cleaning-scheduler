package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // localhost:8080/api/v0.1/shift/cancel-shift/2
    @PutMapping("/cancel-shift/{shiftId}")
    public ResponseEntity<String> cancelShift(@PathVariable("shiftId") Long shiftId) {
        try{
            shiftService.cancelShift(shiftId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("The shift has been successfully cancelled, and associated workers removed from shift.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to cancel shift.");
        }
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