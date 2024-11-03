package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @PutMapping("/{id}")
    public Contract updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        return contractService.updateContract(id, contract);
    }
    @DeleteMapping("/{id}")
    public void deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
    }
}