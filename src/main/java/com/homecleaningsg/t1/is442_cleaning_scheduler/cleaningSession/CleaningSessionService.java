package com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class CleaningSessionService {

    private final CleaningSessionRepository cleaningSessionRepository;
    private final ShiftRepository shiftRepository;
    private final ShiftService shiftService;

    @Autowired
    public CleaningSessionService(CleaningSessionRepository cleaningSessionRepository,
                                  ShiftRepository shiftRepository,
                                  ShiftService shiftService) {
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.shiftRepository = shiftRepository;
        this.shiftService = shiftService;
    }

    public List<CleaningSession> getAllCleaningSessions() {
        return cleaningSessionRepository.findAll();
    }

    public List<CleaningSession> getCleaningSessionsByContractId(Long contractId) {
        return cleaningSessionRepository.findByContract_ContractId(contractId);
    }

    public Optional<CleaningSession> getCleaningSessionByContractIdAndCleaningSessionId(Long contractId, Long cleaningSessionId) {
        return cleaningSessionRepository.findByContract_ContractIdAndCleaningSessionId(contractId, cleaningSessionId);
    }

    public CleaningSession addCleaningSession(CleaningSession cleaningSession){
        Contract contract = cleaningSession.getContract();
        Long durationMinutes = Duration.between(
                cleaningSession.getSessionStartTime(),
                cleaningSession.getSessionEndTime()
        ).toMinutes();


        if (contract.getSessionDurationMinutes() != durationMinutes) {
            throw new IllegalArgumentException("Cleaning session duration must be same as duration set in contract.");
        }

        return cleaningSessionRepository.save(cleaningSession);
    }

    public CleaningSession updateCleaningSession(Long cleaningSessionId, CleaningSessionUpdateDto updatedSessionDto){
        if(!cleaningSessionRepository.existsById(cleaningSessionId)){
            throw new IllegalArgumentException("Cleaning session not found");
        }
        CleaningSession existingSession = cleaningSessionRepository.findById(cleaningSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));

        if (updatedSessionDto.getSessionStartDate() != null) {
            existingSession.setSessionStartDate(updatedSessionDto.getSessionStartDate());
        }
        if (updatedSessionDto.getSessionStartDate() != null) {
            existingSession.setSessionStartDate(updatedSessionDto.getSessionStartDate());
        }
        if (updatedSessionDto.getSessionStartTime() != null) {
            existingSession.setSessionStartTime(updatedSessionDto.getSessionStartTime());
        }
        if (updatedSessionDto.getSessionEndDate() != null) {
            existingSession.setSessionEndDate(updatedSessionDto.getSessionEndDate());
        }
        if (updatedSessionDto.getSessionEndTime() != null) {
            existingSession.setSessionEndTime(updatedSessionDto.getSessionEndTime());
        }
        if (updatedSessionDto.getSessionDescription() != null) {
            existingSession.setSessionDescription(updatedSessionDto.getSessionDescription());
        }
        if (updatedSessionDto.getSessionStatus() != null) {
            existingSession.setSessionStatus(updatedSessionDto.getSessionStatus());
        }
        if (updatedSessionDto.getSessionRating() != null) {
            existingSession.setSessionRating(updatedSessionDto.getSessionRating());
        }
        if (updatedSessionDto.getPlanningStage() != null) {
            existingSession.setPlanningStage(updatedSessionDto.getPlanningStage());
        }
        if (updatedSessionDto.getSessionFeedback() != null) {
            existingSession.setSessionFeedback(updatedSessionDto.getSessionFeedback());
        }
        return cleaningSessionRepository.save(existingSession);
    }

    public void cancelCleaningSession(Long cleaningSessionId) {
        CleaningSession cleaningSession = cleaningSessionRepository.findById(cleaningSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Cleaning session not found"));

        if(cleaningSession.getSessionStatus() == CleaningSession.sessionStatus.CANCELLED){
            throw new IllegalArgumentException("Cleaning session has already been cancelled");
        }
        else if(cleaningSession.getSessionStatus() == CleaningSession.sessionStatus.WORKING){
            throw new IllegalArgumentException("Cleaning session cannot be cancelled as it is ongoing");
        }
        else if(cleaningSession.getSessionStatus() == CleaningSession.sessionStatus.FINISHED){
            throw new IllegalArgumentException("Cleaning session cannot be cancelled because it has already been finished.");
        }
        // sessionStatus == NOT_STARTED
        else{
            cleaningSession.setSessionStatus(CleaningSession.sessionStatus.CANCELLED);
            cleaningSession.setCancelledAt(LocalDate.now());
            // cancel linked shift that has not occurred yet
            List<Shift> shifts = shiftRepository.findByCleaningSession_CleaningSessionId(cleaningSessionId);
            if(!shifts.isEmpty()){
                for(Shift shift : shifts) {
                    // cancel linked shifts has not occurred yet
                    if (shift.getSessionStartDate().isAfter(LocalDate.now())) {
                        Long shiftId = shift.getShiftId();
                        shiftService.cancelShift(shiftId);
                    }
                }
            }
            cleaningSessionRepository.save(cleaningSession);
        }
    }

    public SessionReportDto getMonthlySessionReport(int year, int month) {
        LocalDate startOfMonth = YearMonth.of(year, month).atDay(1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        Long numFinishedSessions = cleaningSessionRepository.countNoOfMonthFinishedSessions(startOfMonth, endOfMonth);
        Long numCancelledSessions = cleaningSessionRepository.countNoOfMonthCancelledSessions(startOfMonth, endOfMonth);

        return new SessionReportDto(numFinishedSessions, numCancelledSessions);
    }

    public SessionReportDto getYearlySessionReport(int year) {
        Long numFinishedSessions = cleaningSessionRepository.countNoOfYearFinishedSessions(year);
        Long numCancelledSessions = cleaningSessionRepository.countNoOfYearCancelledSessions(year);

        return new SessionReportDto(numFinishedSessions, numCancelledSessions);
    }

}