package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSiteRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ContractRepository contractRepository;
    private final ContractService contractService;
    private final LocationService locationService;
    private final ClientSiteRepository clientSiteRepository;
    @Autowired
    public ClientService(ClientRepository clientRepository,
                         ContractRepository contractRepository,
                         ContractService contractService,
                         LocationService locationService,
                         ClientSiteRepository clientSiteRepository) {
        this.clientRepository = clientRepository;
        this.contractRepository = contractRepository;
        this.contractService = contractService;
        this.locationService = locationService;
        this.clientSiteRepository = clientSiteRepository;
    }

    public List<Client> getAllClient(){
        return clientRepository.findAll();
    }

    public Client getClientByName(String name){
        return clientRepository.findByName(name);
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
                if(contract.getContractStart().isAfter(LocalDate.now())){
                    Long contractId = contract.getContractId();
                    contractService.deactivateContract(contractId);
                }
            }
        }
        client.setActive(false);
        client.setDeactivatedAt(LocalDate.now());
        clientRepository.save(client);
    }

    public ClientReportDto getMonthlyClientReport(int year, int month) {
        LocalDate startOfMonth = YearMonth.of(year, month).atDay(1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        Long newClients = clientRepository.countNewClientsByMonth(startOfMonth, endOfMonth);
        Long existingClients = clientRepository.countExistingClientsByMonth(endOfMonth);
        Long terminatedClients = clientRepository.countTerminatedClientsByMonth(startOfMonth, endOfMonth);

        return new ClientReportDto(newClients, existingClients, terminatedClients);
    }

    public ClientReportDto getYearlyClientReport(int year) {
        Long newClients = clientRepository.countNewClientsByYear(year);
        Long existingClients = clientRepository.countExistingClientsByYear(year);
        Long terminatedClients = clientRepository.countTerminatedClientsByYear(year);

        return new ClientReportDto(newClients, existingClients, terminatedClients);
    }

    @Transactional
    public void addClientSiteToClient(
            Long clientId,
            String streetAddress,
            String postalCode,
            String unitNumber,
            Long numberOfRooms,
            ClientSite.PropertyType propertyType
    ) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        addClientSiteToClient(client, streetAddress, postalCode, unitNumber, numberOfRooms, propertyType);
    }

    @Transactional
    public void addClientSiteToClient(
            Client client,
            String streetAddress,
            String postalCode,
            String unitNumber,
            Long numberOfRooms,
            ClientSite.PropertyType propertyType
    ) {
        Location clientLocation = locationService.getOrCreateLocation(postalCode, streetAddress);
        ClientSite clientSite = new ClientSite(client, streetAddress, postalCode, unitNumber, clientLocation, numberOfRooms, propertyType);
        client.addClientSite(clientSite);
        clientSiteRepository.save(clientSite);
    }

    public Client getOrCreateClient(String name, String phone) {
        Client client = clientRepository.findByNameAndPhone(name, phone);
        if (client == null) {
            client = new Client(name, phone);
            client.setActive(true);
            client.setJoinDate(LocalDate.now());
            clientRepository.save(client);
        }
        return client;
    }

    // main endpoint for addClient
    public Client getOrCreateClient(String name,
                                    String phone,
                                    String streetAddress,
                                    String postalCode,
                                    String unitNumber,
                                    Long numberOfRooms,
                                    ClientSite.PropertyType propertyType) {
        Client client = getOrCreateClient(name, phone);
        addClientSiteToClient(client, streetAddress, postalCode, unitNumber, numberOfRooms, propertyType);
        return client;
    }

    public List<ClientWithClientSiteDto> getListOfClientsWithClientSites() {
        List<Client> clients = clientRepository.getClientByActiveEqualsTrue();
        List<ClientWithClientSiteDto> clientWithClientSiteDtos = new ArrayList<>();
        for (Client client : clients) {
            clientWithClientSiteDtos.add(new ClientWithClientSiteDto(client));
        }
        return clientWithClientSiteDtos;
    }
}