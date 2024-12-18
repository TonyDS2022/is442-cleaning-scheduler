package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(path = "api/v0.1/contract")
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public ResponseEntity<?> getContract() {
        try {
            List<Contract> contracts = contractService.getContract();
            List<ContractListViewDto> contractDtos = contracts.stream()
                    .map(ContractListViewDto::new)
                    .toList();
            return ResponseEntity.ok(contractDtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        Contract createdContract = contractService.createContract(contract);
        return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
    }

    @GetMapping("/{contractId}")
    public HttpEntity<?> getContractById(@PathVariable("contractId") Long contractId) {
        try {
            Contract contract = contractService.getContractById(contractId);
            return ResponseEntity.ok(contract);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // localhost:8080/api/v0.1/contract/add-contract
    @PostMapping("/add-contract/")
    public ResponseEntity<String> addContract(
            @RequestParam Long clientId,
            @RequestParam Long clientSiteId,
            @RequestParam LocalDate contractStartDate,
            @RequestParam LocalDate contractEndDate,
            @RequestParam LocalTime sessionStartTime,
            String frequency
            ) {
        try {
            contractService.addContract(clientId, clientSiteId, contractStartDate, contractEndDate, sessionStartTime, frequency);
            return ResponseEntity.status(HttpStatus.OK).body("Contract added successfully.");
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
            return ResponseEntity.status(HttpStatus.OK).body("Contract updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update contract details.");
        }
    }

    // localhost:8080/api/v0.1/contract/deactivate-contract/2
    @PutMapping("/deactivate-contract/{contractId}")
    public ResponseEntity<String> deactivateContract(@PathVariable("contractId") Long contractId) {
        try {
            contractService.deactivateContract(contractId);
            return ResponseEntity.status(HttpStatus.OK).body("The contract has been successfully deactivated, all associated future cleaning sessions and shifts will be cancelled.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to deactivate contract.");
        }
    }
}