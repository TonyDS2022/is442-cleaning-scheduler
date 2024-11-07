package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class ShiftConfig implements CommandLineRunner {

 private final CleaningSessionRepository cleaningSessionRepository;
 private final ShiftRepository shiftRepository;
 private final WorkerRepository workerRepository;

 public ShiftConfig(CleaningSessionRepository cleaningSessionRepository,
                    ShiftRepository shiftRepository,
                    WorkerRepository workerRepository) {
     this.cleaningSessionRepository = cleaningSessionRepository;
     this.shiftRepository = shiftRepository;
     this.workerRepository = workerRepository;
 }

 @Override
 public void run(String... args) throws Exception {
     // Use Integer for querying
     CleaningSession session1 = cleaningSessionRepository.findById(1L).orElseThrow(() -> new IllegalStateException("CleaningSession 1 not found"));
     CleaningSession session2 = cleaningSessionRepository.findById(2L).orElseThrow(() -> new IllegalStateException("CleaningSession 2 not found"));

     Worker worker1 = workerRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Worker 1 not found"));
     Worker worker2 = workerRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Worker 2 not found"));
     Worker worker6 = workerRepository.findById(6L).orElseThrow(() -> new IllegalStateException("Worker 6 not found"));
     Worker worker7 = workerRepository.findById(7L).orElseThrow(() -> new IllegalStateException("Worker 7 not found"));

     Shift shift1 = new Shift(session1);
     shift1.setWorker(worker1);
     shift1.setActualStartDate(LocalDate.of(2024, 1, 1));
     shift1.setActualStartTime(LocalTime.of(9, 0));
     shift1.setActualEndDate(LocalDate.of(2024, 1, 1));
     shift1.setActualEndTime(LocalTime.of(12, 0));

     Shift shift2 = new Shift(session1);
     shift2.setWorker(worker6);

     Shift shift3 = new Shift(session2);
     shift3.setWorker(worker7);

     Shift shift4 = new Shift(session2);
     shift4.setWorker(worker1);
     shift4.setActualStartDate(LocalDate.of(2024, 3, 4));
     shift4.setActualStartTime(LocalTime.of(13, 0));
     shift4.setActualEndDate(LocalDate.of(2024, 3, 4));
     shift4.setActualEndTime(LocalTime.of(17, 0));

     Shift shift5 = new Shift(session2);

     session1.setShifts(List.of(shift1, shift2));
     session2.setShifts(List.of(shift3, shift4,shift5));

     shiftRepository.saveAll(List.of(shift1, shift2, shift3, shift4, shift5));
     cleaningSessionRepository.saveAll(List.of(session1, session2));
 }
}