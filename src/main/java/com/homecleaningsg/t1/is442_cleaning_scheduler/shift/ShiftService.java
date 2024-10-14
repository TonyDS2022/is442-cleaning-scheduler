package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public Optional<Shift> getShiftById(int id) {
        return shiftRepository.findById(id);
    }

    public void addShift(Shift shift) {
        shiftRepository.save(shift);
    }

    public void updateShift(int id, Shift shift) {
        if (shiftRepository.existsById(id)) {
            shift.setShiftId(id);
            shiftRepository.save(shift);
        }
    }

    public void deleteShift(int id) {
        shiftRepository.deleteById(id);
    }

    public List<Shift> getShiftsByWorkerId(Long workerId) {
        return shiftRepository.findByWorkerWorkerId(workerId);
    }
}