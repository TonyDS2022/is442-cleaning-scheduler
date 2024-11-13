package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

<<<<<<< Updated upstream
=======
import com.google.common.util.concurrent.ClosingFuture;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSiteRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
>>>>>>> Stashed changes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
<<<<<<< Updated upstream

    @Autowired
    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
=======
    private final CleaningSessionRepository cleaningSessionRepository;
    private final CleaningSessionService cleaningSessionService;
    private final ClientSiteRepository clientSiteRepository;
    private final ShiftService shiftService;
    private final LocationService locationService;
    private final ClientService clientService;


    @Autowired
    public ContractService(ContractRepository contractRepository,
                           CleaningSessionRepository cleaningSessionRepository,
                           CleaningSessionService cleaningSessionService,
                           ClientSiteRepository clientSiteRepository,
                           ShiftService shiftService,
                           LocationService locationService,
                           ClientService clientService) {
        this.contractRepository = contractRepository;
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.cleaningSessionService = cleaningSessionService;
        this.clientSiteRepository = clientSiteRepository;
        this.shiftService = shiftService;
        this.locationService = locationService;
        this.clientService = clientService;
>>>>>>> Stashed changes
    }

    public List<Contract> getContract() {
        return contractRepository.findAll();
    }

    public Optional<Float> getRateByContractId(int contractId) {
        Optional<Contract> contract = contractRepository.findById(contractId);
        return contract.map(Contract::getRate);
    }
<<<<<<< Updated upstream
=======

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

    public LocalDate getNextDateByFrequency(LocalDate currentDate, Contract.Frequency frequency) {
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
>>>>>>> Stashed changes
}