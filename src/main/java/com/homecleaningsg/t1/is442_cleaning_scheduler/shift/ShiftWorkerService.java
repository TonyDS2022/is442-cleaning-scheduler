
package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ShiftWorkerService {

    private final WorkerRepository workerRepository;
    private final ShiftService shiftService;
    private final WorkerService workerService;

    @Autowired
    public ShiftWorkerService(WorkerRepository workerRepository, ShiftService shiftService, WorkerService workerService) {
        this.workerRepository = workerRepository;
        this.shiftService = shiftService;
        this.workerService = workerService;
    }

    public Location getWorkerLastKnownLocation(Long workerId, LocalDate date, LocalTime time){
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found"));
        LocalTime workerStartTime = worker.getStartWorkingHours();
        Location workerHomeAddress = worker.getHomeLocation();
        if(time.equals(workerStartTime)){
            return workerHomeAddress;
        }

        Shift lastShift = shiftService.findLastShiftOnDayBeforeTime(workerId, date, time);
        if (lastShift != null){
            return lastShift.getLocation();
        }

        return workerHomeAddress;
    }
}