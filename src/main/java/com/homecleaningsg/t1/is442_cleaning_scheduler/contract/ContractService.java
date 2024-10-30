package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

// temp for retrieving all contracts by cleaningSessionIds
import java.util.stream.Collectors;


@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final CleaningSessionRepository cleaningSessionRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, CleaningSessionRepository cleaningSessionRepository) {
        this.contractRepository = contractRepository;
        this.cleaningSessionRepository = cleaningSessionRepository;
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

    public Contract createContract(Contract contract){
        return contractRepository.save(contract);
    }

    public Contract updateContract(Long contractId, Contract updatedContract){
        if(!contractRepository.existsById(contractId)){
            throw new IllegalArgumentException("Contract not found");
        }
        updatedContract.setContractId(contractId);
        return contractRepository.save(updatedContract);
    }

    public void deleteContract(Long contractId){
        if(!contractRepository.existsById(contractId)){
            throw new IllegalArgumentException("Contract not found");
        }
        List<CleaningSession> cleaningSessions = cleaningSessionRepository.findByContract_ContractId(contractId);
        if (!cleaningSessions.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete contract with ID " + contractId + " as there are associated cleaning sessions. Please delete all related cleaning sessions before deleting this contract.");
        }
        contractRepository.deleteById(contractId);
    }

}