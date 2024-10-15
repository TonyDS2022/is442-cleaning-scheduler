package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v0.1/contract")
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public List<Contract> getContract() {
        return contractService.getContract();
    }

    @GetMapping("/{contractId}/rate")
    public Optional<Float> getRateByContractId(@PathVariable Long contractId) {
        return contractService.getRateByContractId(contractId);
    }
}