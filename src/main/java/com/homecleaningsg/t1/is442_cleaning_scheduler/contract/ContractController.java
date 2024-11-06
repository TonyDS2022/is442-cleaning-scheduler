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

    // add contract
    // URL: POST /api/v0.1/contract
    // {
    //     "clientId": 1,
    //         "contractStart": "2024-12-01T00:00:00.000+08:00",
    //         "contractEnd": "2024-12-03T00:00:00.000+012:00",
    //         "contractComment": "Contract Comment",
    //         "ongoing": true,
    //         "price": 100.0,
    //         "workersBudgeted": 1,
    //         "rooms": 3,
    //         "frequency": "DAILY",
    //         "sessionDurationMinutes": 180
    // }
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        Contract createdContract = contractService.createContract(contract);
        return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
    }

    // update contract
    // URL: PUT /api/v0.1/contract/3
    // {
    //     "clientId": 1,
    //     "contractStart": "2024-12-01T00:00:00.000+08:00",
    //     "contractEnd": "2024-12-04T00:00:00.000+08:00",
    //     "contractComment": "Contract Comment Changed",
    //     "price": 100.0,
    //     "workersBudgeted": 1,
    //     "rooms": 3,
    //     "frequency": "DAILY",
    //     "sessionDurationMinutes": 180,
    //     "ongoing": true
    // }
    // @PutMapping("/{id}")
    // public ResponseEntity<Contract> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
    //     Contract updatedContract = contractService.updateContract(id, contract);
    //     return new ResponseEntity<>(updatedContract, HttpStatus.OK);
    // }

    // delete contract
    // URL: DELETE /api/v0.1/contract/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("The contract has been successfully deactivated, along with all associated future cleaning sessions and shifts.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to deactivate contract.");
        }
    }
}