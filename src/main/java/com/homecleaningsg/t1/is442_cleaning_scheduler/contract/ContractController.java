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

    @PostMapping("/create-contract/")
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        try {
            Contract createdContract = contractService.createContract(contract);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdContract); // Return 201 Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Return 400 Bad Request
        }
    }

    @PutMapping("/update-contract/{contractId}")
    public ResponseEntity<Contract> updateContract(
            @PathVariable("contractId") Long contractId, @RequestBody Contract updatedContract) {
        try {
            Contract updatedContractResponse = contractService.updateContract(contractId, updatedContract);
            return ResponseEntity.ok(updatedContractResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // localhost:8080/api/v0.1/contract/delete-contract/1
    @DeleteMapping("/delete-contract/{contractId}")
    public ResponseEntity<String> deleteContract(@PathVariable("contractId") Long contractId) {
        try {
            contractService.deleteContract(contractId);
            return ResponseEntity.ok("Contract is deleted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}