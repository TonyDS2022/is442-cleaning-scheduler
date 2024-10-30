package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
     CleaningSession session1 = cleaningSessionRepository.findById(1).orElseThrow(() -> new IllegalStateException("CleaningSession 1 not found"));
     CleaningSession session2 = cleaningSessionRepository.findById(2).orElseThrow(() -> new IllegalStateException("CleaningSession 2 not found"));
     CleaningSession session3 = cleaningSessionRepository.findById(3).orElseThrow(() -> new IllegalStateException("CleaningSession 3 not found"));

     Worker worker1 = workerRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Worker 1 not found"));
     Worker worker2 = workerRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Worker 2 not found"));
     Worker worker6 = workerRepository.findById(6L).orElseThrow(() -> new IllegalStateException("Worker 6 not found"));

     Shift shift1 = new Shift(session1);
     shift1.setWorker(worker1);

     Shift shift2 = new Shift(session2);
     shift2.setWorker(worker2);

     Shift shift3 = new Shift(session2);
     shift3.setWorker(worker6);

     Shift shift4 = new Shift(session2);

     // assign to session on 11-06-2024 to test leaveApplication clash
     Shift shift5 = new Shift(session3);
     shift5.setWorker(worker1);

     shiftRepository.saveAll(List.of(shift1, shift2, shift3, shift4, shift5));

     session1.setShifts(List.of(shift1, shift2));
     session2.setShifts(List.of(shift3, shift4));
     session3.setShifts(List.of(shift5));
     cleaningSessionRepository.saveAll(List.of(session1, session2, session3));
 }
}