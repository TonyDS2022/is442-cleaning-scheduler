package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<Shift> getShiftById(@PathVariable("shiftId") int shiftId) {
        return shiftService.getShiftById(shiftId);
    }

    @PostMapping
    public void addShift(@RequestBody Shift shift) {
        shiftService.addShift(shift);
    }

    @PutMapping("/{shiftId}")
    public void updateShift(@PathVariable("shiftId") int shiftId, @RequestBody Shift shift) {
        shiftService.updateShift(shiftId, shift);
    }

    @DeleteMapping("/{shiftId}")
    public void deleteShift(@PathVariable("shiftId") int shiftId) {
        shiftService.deleteShift(shiftId);
    }

    @GetMapping("/worker/{workerId}")
    public List<Shift> getShiftsByWorkerId(@PathVariable("workerId") Long workerId) {
        return shiftService.getShiftsByWorkerId(workerId);
    }
}