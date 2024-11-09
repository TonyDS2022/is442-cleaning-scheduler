package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add-contract/")
    public ResponseEntity<String> addContract(@RequestBody Contract contract) {
        try {
            contractService.addContract(contract);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Contract added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add contract.");
        }
    }

    // localhost:8080/api/v0.1/contract/update-contract/1
    @PutMapping("/update-contract/{contractId}")
    public ResponseEntity<String> updateContract(
            @PathVariable("contractId") Long contractId, @RequestBody Contract updatedContract) {
        try {
            contractService.updateContract(contractId, updatedContract);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Contract updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update contract details.");
        }
    }

    // localhost:8080/api/v0.1/contract/deactivate-contract/2
    @PutMapping("/deactivate-contract/{contractId}")
    public ResponseEntity<String> deactivateContract(@PathVariable("contractId") Long contractId) {
        try {
            contractService.deactivateContract(contractId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("The contract has been successfully deactivated, all associated future cleaning sessions and shifts will be cancelled.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to deactivate contract.");
        }
    }
}