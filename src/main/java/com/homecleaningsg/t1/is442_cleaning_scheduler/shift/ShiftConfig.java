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
                    WorkerRepository workerRepository
 ) {
     this.cleaningSessionRepository = cleaningSessionRepository;
     this.shiftRepository = shiftRepository;
     this.workerRepository = workerRepository;
 }

 @Override
 public void run(String... args) throws Exception {
     // Use Integer for querying
     CleaningSession session1 = cleaningSessionRepository.findById(1L).orElseThrow(() -> new IllegalStateException("CleaningSession 1 not found"));
     CleaningSession session2 = cleaningSessionRepository.findById(2L).orElseThrow(() -> new IllegalStateException("CleaningSession 2 not found"));
     CleaningSession session3 = cleaningSessionRepository.findById(3L).orElseThrow(() -> new IllegalStateException("CleaningSession 3 not found"));

     Worker worker1 = workerRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Worker 1 not found"));
     // Worker worker2 = workerRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Worker 2 not found"));
     Worker worker6 = workerRepository.findById(6L).orElseThrow(() -> new IllegalStateException("Worker 6 not found"));
     Worker worker7 = workerRepository.findById(7L).orElseThrow(() -> new IllegalStateException("Worker 7 not found"));

     // These two shifts are part of the same cleaning session,
     // meaning they share the same start and end times (auto assigned in Shift.java)
     // A shift represents a worker's involvement in the cleaning session,
     // and is used to denote the cleaning session for each worker.

     Shift shift1 = new Shift(session1);
     shift1.setWorker(worker1);
     // this is the actual time that the worker arrive to the shift
     shift1.setActualStartDate(LocalDate.of(2024, 10, 5));
     shift1.setActualStartTime(LocalTime.of(9, 0));
     shift1.setActualEndDate(LocalDate.of(2024, 10, 5));
     shift1.setActualEndTime(LocalTime.of(12, 0));

     Shift shift2 = new Shift(session1);
     shift2.setWorker(worker6);
     // this is the actual time that the worker arrive to the shift - worker6 is one hour late
     shift2.setActualStartDate(LocalDate.of(2024, 10, 5));
     shift2.setActualStartTime(LocalTime.of(10, 0));
     shift2.setActualEndDate(LocalDate.of(2024, 10, 5));
     shift2.setActualEndTime(LocalTime.of(12, 0));


     Shift shift3 = new Shift(session2);
     shift3.setWorker(worker7);
     shift3.setActualStartDate(LocalDate.of(2024, 10, 12));
     shift3.setActualStartTime(LocalTime.of(14, 0));
     shift3.setActualEndDate(LocalDate.of(2024, 10, 12));
     shift3.setActualEndTime(LocalTime.of(17, 0));

     Shift shift4 = new Shift(session2);
     shift4.setWorker(worker1);
     shift4.setActualStartDate(LocalDate.of(2024, 10, 12));
     shift4.setActualStartTime(LocalTime.of(14, 5));
     shift4.setActualEndDate(LocalDate.of(2024, 10, 12));
     shift4.setActualEndTime(LocalTime.of(17, 5));

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