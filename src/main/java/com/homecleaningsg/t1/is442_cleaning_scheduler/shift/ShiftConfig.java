// package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;
//
// import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
// import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
// import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
// import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;
//
// import java.sql.Timestamp;
// import java.text.SimpleDateFormat;
// import java.util.List;
//
// @Component
// public class ShiftConfig implements CommandLineRunner {
//
//     private final CleaningSessionRepository cleaningSessionRepository;
//     private final ShiftRepository shiftRepository;
//     private final WorkerRepository workerRepository;
//
//     public ShiftConfig(CleaningSessionRepository cleaningSessionRepository,
//                        ShiftRepository shiftRepository,
//                        WorkerRepository workerRepository) {
//         this.cleaningSessionRepository = cleaningSessionRepository;
//         this.shiftRepository = shiftRepository;
//         this.workerRepository = workerRepository;
//     }
//
//     @Override
//     public void run(String... args) throws Exception {
//         CleaningSession session1 = cleaningSessionRepository.findById(1).orElseThrow(() -> new IllegalStateException("CleaningSession 1 not found"));
//         CleaningSession session2 = cleaningSessionRepository.findById(2).orElseThrow(() -> new IllegalStateException("CleaningSession 2 not found"));
//
//         Worker worker1 = workerRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Worker 1 not found"));
//         Worker worker2 = workerRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Worker 2 not found"));
//
//         SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
//
//         Shift shift1 = new Shift(
//                 worker1,
//                 session1,
//                 new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                 new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                 Shift.WorkingStatus.WORKING
//         );
//
//         Shift shift2 = new Shift(
//                 worker2,
//                 session1,
//                 new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                 new Timestamp(dateFormat.parse("05 Oct 2024").getTime()),
//                 Shift.WorkingStatus.WORKING
//         );
//
//         Shift shift3 = new Shift(
//                 worker1,
//                 session2,
//                 new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                 new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                 Shift.WorkingStatus.NOT_STARTED
//         );
//
//         Shift shift4 = new Shift(
//                 worker2,
//                 session2,
//                 new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                 new Timestamp(dateFormat.parse("12 Oct 2024").getTime()),
//                 Shift.WorkingStatus.NOT_STARTED
//         );
//
//         session1.setShifts(List.of(shift1, shift2));
//         session2.setShifts(List.of(shift3, shift4));
//
//         shiftRepository.saveAll(List.of(shift1, shift2, shift3, shift4));
//         cleaningSessionRepository.saveAll(List.of(session1, session2));
//     }
// }

package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
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

     Worker worker1 = workerRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Worker 1 not found"));
     Worker worker2 = workerRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Worker 2 not found"));

     SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

     Shift shift1 = new Shift(session1);
     shift1.setWorker(worker1);

     Shift shift2 = new Shift(session1);
     shift2.setWorker(worker2);

     Shift shift3 = new Shift(session2);
     shift3.setWorker(worker1);

     Shift shift4 = new Shift(session2);
     shift4.setWorker(worker2);

     session1.setShifts(List.of(shift1, shift2));
     session2.setShifts(List.of(shift3, shift4));

     shiftRepository.saveAll(List.of(shift1, shift2, shift3, shift4));
     cleaningSessionRepository.saveAll(List.of(session1, session2));
 }
}