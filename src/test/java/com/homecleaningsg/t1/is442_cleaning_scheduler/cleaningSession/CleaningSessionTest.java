package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CleaningSessionTest {

    @Mock
    private Shift shift1;

    @Mock
    private Shift shift2;

    private CleaningSession cleaningSession;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Contract contract = new Contract();
        contract.setWorkersBudgeted(2);
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        cleaningSession = new CleaningSession(
                contract,
                nowDate,
                nowTime,
                nowDate,
                nowTime,
                "Test Session",
                CleaningSession.sessionStatus.NOT_STARTED
        );
    }

    @Test
    public void testPlanningStageGreen() {
        when(shift1.getWorker()).thenReturn(new Worker());
        when(shift2.getWorker()).thenReturn(new Worker());
        when(shift1.isWorkerHasPendingLeave()).thenReturn(false);
        when(shift2.isWorkerHasPendingLeave()).thenReturn(false);

        cleaningSession.setShifts(Arrays.asList(shift1, shift2));

        assertEquals(CleaningSession.PlanningStage.GREEN, cleaningSession.getPlanningStage());
    }

    @Test
    public void testGetPlanningStageEmber() {
        when(shift1.getWorker()).thenReturn(new Worker());
        when(shift2.getWorker()).thenReturn(new Worker());
        when(shift1.isWorkerHasPendingLeave()).thenReturn(true);
        when(shift2.isWorkerHasPendingLeave()).thenReturn(false);

        cleaningSession.setShifts(Arrays.asList(shift1, shift2));

        assertEquals(CleaningSession.PlanningStage.EMBER, cleaningSession.getPlanningStage());
    }

    @Test
    public void testGetPlanningStageRed() {
        when(shift1.getWorker()).thenReturn(new Worker());
        when(shift2.getWorker()).thenReturn(null);
        when(shift1.isWorkerHasPendingLeave()).thenReturn(false);
        when(shift2.isWorkerHasPendingLeave()).thenReturn(false);

        cleaningSession.setShifts(Arrays.asList(shift1, shift2));

        assertEquals(CleaningSession.PlanningStage.RED, cleaningSession.getPlanningStage());
    }
}
