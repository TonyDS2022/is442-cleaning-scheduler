package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSession;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.Client;
import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientContractService {

    private final ClientService clientService;
    private final ContractService contractService;
    private final LocationService locationService;
    private final ContractRepository contractRepository;
    private final CleaningSessionService cleaningSessionService;
    private final ShiftService shiftService;

    @Autowired
    public ClientContractService(ClientService clientService,
                                 ContractService contractService,
                                 LocationService locationService,
                                 ContractRepository contractRepository,
                                 CleaningSessionService cleaningSessionService,
                                 ShiftService shiftService){
        this.clientService = clientService;
        this.contractService = contractService;
        this.locationService = locationService;
        this.contractRepository = contractRepository;
        this.cleaningSessionService = cleaningSessionService;
        this.shiftService = shiftService;
    }

    public Contract addContract(Contract contract){
        Location location = contract.getClientSite().getLocation();
        locationService.getOrCreateLocation(location.getPostalCode(), location.getAddress());
        Client client = contract.getClient();
        clientService.getOrCreateClient(client.getName(), client.getPhone());
        contractRepository.save(contract);
        List<LocalDate> daysToSchedule = new ArrayList<>();
        LocalDate currentDate = contract.getContractStart();
        // Loop until we reach the contract end date
        while (!currentDate.isAfter(contract.getContractEnd())) {
            // Loop until we reach or exceed the contract end date
            while (!currentDate.isAfter(contract.getContractEnd())) {
                // Add the current date to the list as a session day
                daysToSchedule.add(currentDate);
                // Increment currentDate based on contract frequency
                currentDate = contractService.getNextDateByFrequency(currentDate, contract.getFrequency());
            }
            // Increment currentDate based on contract frequency
            currentDate = contractService.getNextDateByFrequency(currentDate, contract.getFrequency());
        }
        // Create CleaningSessions and Shifts for each calculated day
        int sessionCounter = 1; // Initialize a counter to label the sessions
        for (LocalDate day : daysToSchedule) {
            // Construct the session description with the session number (e.g., "Session 1", "Session 2", etc.)
            String sessionDescription = "Session " + sessionCounter++;
            // Create a CleaningSession based on the day and contract details
            CleaningSession newCleaningSession = new CleaningSession(
                    contract,
                    day,
                    contract.getSessionStartTime(),
                    day,
                    contract.getSessionEndTime(),
                    sessionDescription,
                    contract.getWorkersBudgeted()
            );
            cleaningSessionService.addCleaningSession(newCleaningSession);
            // Create Shifts based on the workers budgeted in the contract
            for (int i = 0; i < contract.getWorkersBudgeted(); i++) {
                Shift newShift = new Shift(newCleaningSession);
                shiftService.addShift(newShift);
            }
        }
        return contract;
    }
    public Contract setUpClientContract(Contract contract){
        Client client = contract.getClient();
        clientService.getOrCreateClient(
                client.getName(),
                client.getPhone(),
                contract.getClientSite().getStreetAddress(),
                contract.getClientSite().getPostalCode(),
                contract.getClientSite().getUnitNumber());
        addContract(contract);
        return contract;
    }
}
