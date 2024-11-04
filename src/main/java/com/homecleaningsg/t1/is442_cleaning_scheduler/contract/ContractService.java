package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// temp for retrieving all contracts by cleaningSessionIds
import java.util.stream.Collectors;


@Service
public class ContractService {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public List<Contract> getContract() {
        return contractRepository.findAll();
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

    public Contract updateContract(Long id, Contract contract) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        existingContract.setClientId(contract.getClientId());
        existingContract.setContractStart(contract.getContractStart());
        existingContract.setContractEnd(contract.getContractEnd());
        existingContract.setContractComment(contract.getContractComment());
        existingContract.setOngoing(contract.isOngoing());
        existingContract.setPrice(contract.getPrice());
        existingContract.setWorkersBudgeted(contract.getWorkersBudgeted());
        existingContract.setRooms(contract.getRooms());
        existingContract.setFrequency(contract.getFrequency());
        existingContract.setSessionDurationMinutes(contract.getSessionDurationMinutes());
        return contractRepository.save(existingContract);
    }
    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }
}