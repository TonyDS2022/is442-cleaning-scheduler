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

    public Optional<Float> getRateByContractId(int contractId) {
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
    public List<Contract> getContractsByCleaningSessionIds(List<Integer> cleaningSessionIds) {
        return contractRepository.findAll().stream()
                .filter(contract -> contract.getCleaningSessions().stream()
                        .anyMatch(session -> cleaningSessionIds.contains(session.getCleaningSessionId())))
                .collect(Collectors.toList());
    }
}