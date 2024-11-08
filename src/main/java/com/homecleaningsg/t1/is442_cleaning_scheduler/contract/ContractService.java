package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.admin.ClientReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.admin.ContractReportDto;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

// temp for retrieving all contracts by cleaningSessionIds
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
        Instant now = Instant.now();
        List<Contract> contracts = contractRepository.findAll();

        for (Contract contract : contracts) {
            if (contract.getContractEnd().toInstant().isBefore(now) &&
                    contract.getContractStatus() != Contract.ContractStatus.COMPLETED) {
                contract.setContractStatus(Contract.ContractStatus.COMPLETED);
                contractRepository.save(contract);
            } else if (contract.getContractStart().toInstant().isBefore(now) &&
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

    // // temp for retrieving all contracts by cleaningSessionIds
    // public List<Contract> getContractsByCleaningSessionIds(List<Integer> cleaningSessionIds) {
    //     return contractRepository.findAll().stream()
    //             .filter(contract -> contract.getCleaningSessions().stream()
    //                     .anyMatch(session -> cleaningSessionIds.contains(session.getCleaningSessionId())))
    //             .collect(Collectors.toList());
    // }
    public List<Contract> getContractsByCleaningSessionIds(List<Long> cleaningSessionIds) {
        return contractRepository.findAll().stream()
                .filter(contract -> contract.getCleaningSessions().stream()
                        .anyMatch(session -> cleaningSessionIds.contains(session.getCleaningSessionId())))
                .collect(Collectors.toList());
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
                // deactivate linked cleaning session and shift that has not occurred yet
                if (cleaningSession.getSessionStartDate().isAfter(LocalDate.now())) {
                    Long cleaningSessionId = cleaningSession.getCleaningSessionId();
                    cleaningSessionService.deactivateCleaningSession(cleaningSessionId);
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
        Long existingOngoingContracts = contractRepository.countExistingOngoingContractsByMonth(startOfMonth);

        return new ContractReportDto(newContracts, existingOngoingContracts);
    }
}