package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final CleaningSessionRepository cleaningSessionRepository;
    private final CleaningSessionService cleaningSessionService;

    @Autowired
    public ContractService(ContractRepository contractRepository, CleaningSessionRepository cleaningSessionRepository, CleaningSessionService cleaningSessionService) {
        this.contractRepository = contractRepository;
        this.cleaningSessionRepository = cleaningSessionRepository;
        this.cleaningSessionService = cleaningSessionService;
    }

    // updates contractStatus automatically when all contracts are retrieved
    // based on current date and contractStart and contractEnd dates
    public List<Contract> getContract() {
        List<Contract> contracts = contractRepository.findAll();

        for (Contract contract : contracts) {
            if (contract.getContractEnd().isBefore(LocalDate.now()) &&
                    contract.getContractStatus() != Contract.ContractStatus.COMPLETED) {
                contract.setContractStatus(Contract.ContractStatus.COMPLETED);
                contractRepository.save(contract);
            } else if (contract.getContractStart().isBefore(LocalDate.now()) &&
                    contract.getContractStatus() == Contract.ContractStatus.NOT_STARTED) {
                contract.setContractStatus(Contract.ContractStatus.IN_PROGRESS);
                contractRepository.save(contract);
            }
        }

        return contracts;
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

    public Contract addContract(Contract contract){
        return contractRepository.save(contract);
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
}