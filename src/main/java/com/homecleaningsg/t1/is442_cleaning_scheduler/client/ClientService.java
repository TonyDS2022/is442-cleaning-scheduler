package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ContractRepository contractRepository;
    private final ContractService contractService;

    @Autowired
    public ClientService(ClientRepository clientRepository,
                         ContractRepository contractRepository,
                         ContractService contractService){
        this.clientRepository = clientRepository;
        this.contractRepository = contractRepository;
        this.contractService = contractService;
    }

    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long clientId){
        return clientRepository.findById(clientId);
    }

    public Client getClientByName(String name){
        return clientRepository.findByName(name);
    }

    public Client addClient(Client client){
        return clientRepository.save(client);
    }

    public Client updateClient(Long clientId, Client updatedClient) {
        if(!clientRepository.existsById(clientId)){
            throw new IllegalArgumentException("Client not found");
        }
        updatedClient.setClientId(clientId);
        return clientRepository.save(updatedClient);
    }

    public void deactivateClient(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        List<Contract> contracts = contractRepository.findByClient(client);
        if(!contracts.isEmpty()){
            for(Contract contract: contracts){
                // deactivate linked contract, cleaning session and shift that has not occurred yet
                if(contract.getContractStart().toLocalDateTime().toLocalDate().isAfter(LocalDate.now())){
                    Long contractId = contract.getContractId();
                    contractService.deactivateContract(contractId);
                }
            }
        }
        client.setActive(false);
        clientRepository.save(client);
    }}
