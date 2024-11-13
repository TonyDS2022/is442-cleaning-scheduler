package com.homecleaningsg.t1.is442_cleaning_scheduler.shift;

import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplicationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.Trip;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ShiftServiceTest {

    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private WorkerRepository workerRepository;
    @Mock
    private WorkerService workerService;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private LeaveApplicationRepository leaveApplicationRepository;

    @InjectMocks
    private ShiftService shiftService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAvailableWorkersForShift() {
        // Define test shift data
        Long shiftId = 1L;
        Shift testShift = new Shift();
        testShift.setShiftId(shiftId);
        testShift.setSessionStartTime(LocalTime.of(9, 0));
        testShift.setSessionEndTime(LocalTime.of(17, 0));
        testShift.setSessionStartDate(LocalDate.now());
        Location shiftLocation = new Location();
        shiftLocation.setAddress("Shift Location");
        ClientSite clientSite = new ClientSite();
        clientSite.setStreetAddress(shiftLocation.getAddress());
        clientSite.setLocation(shiftLocation);
        testShift.setClientSite(clientSite);

        // Define workers
        Worker worker1 = new Worker();
        worker1.setWorkerId(1L);
        worker1.setName("Worker 1");
        worker1.setStartWorkingHours(LocalTime.of(8, 0));
        worker1.setEndWorkingHours(LocalTime.of(18, 0));
        worker1.setHomeLocation(new Location("Worker 1 Home", "123456"));

        Worker worker2 = new Worker();
        worker2.setWorkerId(2L);
        worker2.setName("Worker 2");
        worker2.setStartWorkingHours(LocalTime.of(8, 0));
        worker2.setEndWorkingHours(LocalTime.of(18, 0));
        worker2.setHomeLocation(new Location("Worker 2 Home", "654321"));

        List<Worker> allWorkers = Arrays.asList(worker1, worker2);

        // Define trips between worker locations and shift location
        Trip trip1 = new Trip();
        trip1.setTripDurationSeconds(900);  // 15 minutes
        trip1.setTripDistanceMeters(5000);  // 5 km

        Trip trip2 = new Trip();
        trip2.setTripDurationSeconds(1200);  // 20 minutes
        trip2.setTripDistanceMeters(8000);   // 8 km

        // Mock repository behavior
        when(shiftRepository.findById(shiftId)).thenReturn(Optional.of(testShift));
        when(workerRepository.findByStartWorkingHoursBeforeEndWorkingHoursAfter(any(LocalTime.class), any(LocalTime.class)))
                .thenReturn(allWorkers);
        when(shiftRepository.findBySessionStartTimeBetween(any(LocalTime.class), any(LocalTime.class)))
                .thenReturn(Collections.emptyList());  // No overlapping shifts with start time
        when(shiftRepository.findBySessionEndTimeBetween(any(LocalTime.class), any(LocalTime.class)))
                .thenReturn(Collections.emptyList());  // No overlapping shifts with end time
        when(workerService.workerHasPendingOrApprovedLeaveBetween(anyLong(), any(LocalDate.class),any(LocalDate.class)))
                .thenReturn(false);  // Workers are not on leave
        when(tripRepository.findTripByOriginAndDestination(worker1.getHomeLocation(), shiftLocation)).thenReturn(trip1);
        when(tripRepository.findTripByOriginAndDestination(worker2.getHomeLocation(), shiftLocation)).thenReturn(trip2);

        // Call method under test
        List<AvailableWorkerDto> result = shiftService.getAvailableWorkersForShift(shiftId);

        // Assert expected results
        assertEquals(2, result.size());
        AvailableWorkerDto worker1Dto = result.stream().filter(dto -> dto.getWorkerId().equals(worker1.getWorkerId())).findFirst().orElse(null);
        assertNotNull(worker1Dto);
        assertEquals("Worker 1", worker1Dto.getWorkerName());
        assertEquals("Worker 1 Home", worker1Dto.getWorkerLastLocation());
        assertEquals("Shift Location", worker1Dto.getDestination());
        assertEquals(900, worker1Dto.getTripDurationSeconds());
        assertEquals(5000, worker1Dto.getTripDistanceMeters());

        AvailableWorkerDto worker2Dto = result.stream().filter(dto -> dto.getWorkerId().equals(worker2.getWorkerId())).findFirst().orElse(null);
        assertNotNull(worker2Dto);
        assertEquals("Worker 2", worker2Dto.getWorkerName());
        assertEquals("Worker 2 Home", worker2Dto.getWorkerLastLocation());
        assertEquals("Shift Location", worker2Dto.getDestination());
        assertEquals(1200, worker2Dto.getTripDurationSeconds());
        assertEquals(8000, worker2Dto.getTripDistanceMeters());
    }
}