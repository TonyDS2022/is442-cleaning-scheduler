package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.google.common.util.concurrent.ClosingFuture;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSiteRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final CleaningSessionRepository cleaningSessionRepository;
    private final CleaningSessionService cleaningSessionService;
    private final ClientSiteRepository clientSiteRepository;
    private final LocationRepository locationRepository;
    private final ShiftService shiftService;

    @Autowired
    public ContractService(ContractRepository contractRepository, CleaningSessionRepository cleaningSessionRepository, CleaningSessionService cleaningSessionService, ClientSiteRepository clientSiteRepository, LocationRepository locationRepository, ShiftService shiftService) {
        this.contractRepository = contractRepository;
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.cleaningSessionService = cleaningSessionService;
        this.clientSiteRepository = clientSiteRepository;
        this.locationRepository = locationRepository;
        this.shiftService = shiftService;
    }

    // updates contractStatus automatically when all contracts are retrieved
    // based on current date and contractStart and contractEnd dates
    public List<Contract> getContract() {
        return contractRepository.findAll();
    }

    public Optional<Float> getRateByContractId(Long contractId) {
        Optional<Contract> contract = contractRepository.findById(contractId);
        return contract.map(Contract::getRate);
    }

    public List<Contract> getContractsByCleaningSessionIds(List<Long> cleaningSessionIds) {
        return contractRepository.findAll().stream()
                .filter(contract -> contract.getCleaningSessions().stream()
                        .anyMatch(session -> cleaningSessionIds.contains(session.getCleaningSessionId())))
                .collect(Collectors.toList());
    }

    public Contract getContractById(Long contractId){
        return contractRepository.findById(contractId).orElseThrow(() -> new IllegalArgumentException("Contract not found"));
    }

    // Create, Update, Delete Contract
    public Contract createContract(Contract contract) {
        return contractRepository.save(contract);
    }

    // public Contract updateContract(Long id, Contract contract) {
    //     Contract existingContract = contractRepository.findById(id)
    //             .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
    //     existingContract.setClientId(contract.getClientId());
    //     existingContract.setContractStart(contract.getContractStart());
    //     existingContract.setContractEnd(contract.getContractEnd());
    //     existingContract.setContractComment(contract.getContractComment());
    //     existingContract.setOngoing(contract.isOngoing());
    //     existingContract.setPrice(contract.getPrice());
    //     existingContract.setWorkersBudgeted(contract.getWorkersBudgeted());
    //     existingContract.setRooms(contract.getRooms());
    //     existingContract.setFrequency(contract.getFrequency());
    //     existingContract.setSessionDurationMinutes(contract.getSessionDurationMinutes());
    //     return contractRepository.save(existingContract);
    // }
    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }

    private LocalDate getNextDateByFrequency(LocalDate currentDate, Contract.Frequency frequency) {
        return switch (frequency) {
            case DAILY -> currentDate.plusDays(1);
            case WEEKLY -> currentDate.plusWeeks(1);
            case BIWEEKLY -> currentDate.plusWeeks(2);
            case MONTHLY -> currentDate.plusMonths(1);
            case BIMONTHLY -> currentDate.plusMonths(2);
            case QUARTERLY -> currentDate.plusMonths(3);
            case ANNUALLY -> currentDate.plusYears(1);
            default -> throw new IllegalArgumentException("Unsupported frequency: " + frequency);
        };
    }

    public Contract addContract(Contract contract){
        locationRepository.save(contract.getClientSite().getLocation());
        clientSiteRepository.save(contract.getClientSite());
        contractRepository.save(contract);
        List<LocalDate> daysToSchedule = new ArrayList<>();
        LocalDate currentDate = contract.getContractStart();
        // Loop until we reach the contract end date
        while (!currentDate.isAfter(contract.getContractEnd())) {
            // Loop until we reach or exceed the contract end date
            while (!currentDate.isAfter(contract.getContractEnd())) {
                // Add the current date to the list as a session day
                daysToSchedule.add(currentDate);
                // Increment currentDate based on contract frequency
                currentDate = getNextDateByFrequency(currentDate, contract.getFrequency());
            }
            // Increment currentDate based on contract frequency
            currentDate = getNextDateByFrequency(currentDate, contract.getFrequency());
        }
        // Create CleaningSessions and Shifts for each calculated day
        int sessionCounter = 1; // Initialize a counter to label the sessions
        for (LocalDate day : daysToSchedule) {
            // Construct the session description with the session number (e.g., "Session 1", "Session 2", etc.)
            String sessionDescription = "Session " + sessionCounter++;
            // Create a CleaningSession based on the day and contract details
            CleaningSession newCleaningSession = new CleaningSession(
                    contract,
                    day,
                    contract.getSessionStartTime(),
                    day,
                    contract.getSessionEndTime(),
                    sessionDescription,
                    contract.getWorkersBudgeted()
            );
            cleaningSessionService.addCleaningSession(newCleaningSession);
            // Create Shifts based on the workers budgeted in the contract
            for (int i = 0; i < contract.getWorkersBudgeted(); i++) {
                Shift newShift = new Shift(newCleaningSession);
                shiftService.addShift(newShift);
            }
        }
        return contract;
    }

    public Contract updateContract(Long contractId, Contract updatedContract){
        if(!contractRepository.existsById(contractId)){
            throw new IllegalArgumentException("Contract not found");
        }
        updatedContract.setContractId(contractId);
        return contractRepository.save(updatedContract);
    }

    public void deactivateContract(Long contractId){
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        List<CleaningSession> cleaningSessions = cleaningSessionRepository.findByContract_ContractId(contractId);
        if(!cleaningSessions.isEmpty()){
            for(CleaningSession cleaningSession : cleaningSessions) {
                // cancel linked cleaning session and shift that has not occurred yet
                if (cleaningSession.getSessionStartDate().isAfter(LocalDate.now())) {
                    Long cleaningSessionId = cleaningSession.getCleaningSessionId();
                    cleaningSessionService.cancelCleaningSession(cleaningSessionId);
                }
            }
        }
        // deactivate contract
        contract.setActive(false);
        contractRepository.save(contract);
    }

    public ContractReportDto getMonthlyContractReport(int year, int month) {
        LocalDate startOfMonth = YearMonth.of(year, month).atDay(1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        Long newContracts = contractRepository.countNewContractsByMonth(startOfMonth, endOfMonth);
        Long existingOngoingContracts = contractRepository.countExistingContractsByMonth(startOfMonth, endOfMonth);
        Long completedContracts = contractRepository.countCompletedContractsByMonth(startOfMonth, endOfMonth);

        return new ContractReportDto(newContracts, existingOngoingContracts, completedContracts);
    }

    public ContractReportDto getYearlyContractReport(int year) {
        Long newContracts = contractRepository.countNewContractsByYear(year);
        Long existingOngoingContracts = contractRepository.countExistingContractsByYear(year);
        Long completedContracts = contractRepository.countCompletedContractsByYear(year);

        return new ContractReportDto(newContracts, existingOngoingContracts, completedContracts);
    }

    public Long getNumberOfHours(Long numberOfRooms, ClientSite.PropertyType propertyType) {
        if (propertyType == ClientSite.PropertyType.HDB) {
            return numberOfRooms;
        } else if (propertyType == ClientSite.PropertyType.CONDOMINIUM) {
            return numberOfRooms + 1;
        }
        throw new IllegalArgumentException("Invalid property type or number of rooms");
    }
}