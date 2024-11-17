package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v0.1/client")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{name}")
    public Client getClientByName(@PathVariable("name") String name) {
        return clientService.getClientByName(name);
    }

    // localhost:8080/api/v0.1/client/add-client/
    @PostMapping("/add-client/")
    public ResponseEntity<String> addClient(@RequestParam String name,
                                            @RequestParam String phone,
                                            @RequestParam String homeAddress,
                                            @RequestParam String postalCode,
                                            @RequestParam String unitNumber,
                                            @RequestParam Long numberOfRooms,
                                            @RequestParam ClientSite.PropertyType propertyType
                                            ) {
        try {
            clientService.getOrCreateClient(name, phone, homeAddress, postalCode, unitNumber, numberOfRooms, propertyType);
            return ResponseEntity.status(HttpStatus.OK).body("Client added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add client.");
        }
    }

    @PostMapping("/{clientId}/add-client-site/")
    public ResponseEntity<String> addClientSite(
            @PathVariable("clientId") Long clientId,
            @RequestParam String streetAddress,
            @RequestParam String postalCode,
            @RequestParam String unitNumber,
            @RequestParam Long numberOfRooms,
            @RequestParam ClientSite.PropertyType propertyType
            ) {
        try {
            clientService.addClientSiteToClient(
                    clientId,
                    streetAddress,
                    postalCode,
                    unitNumber,
                    numberOfRooms,
                    propertyType
            );
            return ResponseEntity.status(HttpStatus.OK).body("Client site added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add client site.");
        }
    }

    @PutMapping("/update-client/{clientId}")
    public ResponseEntity<String> updateClient(
            @PathVariable("clientId") Long clientId, @RequestBody Client updatedClient) {
        try {
            clientService.updateClient(clientId, updatedClient);
            return ResponseEntity.status(HttpStatus.OK).body("Client updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update client details.");
        }
    }

    // localhost:8080/api/v0.1/client/deactivate-client/1
    @PutMapping("/deactivate-client/{clientId}")
    public ResponseEntity<String> deactivateClient(@PathVariable("clientId") Long clientId) {
        try {
            clientService.deactivateClient(clientId);
            return ResponseEntity.status(HttpStatus.OK).body("Client has been deactivated successfully, along with all associated future contracts, cleaning sessions and shifts.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to deactivate client.");
        }
    }

    // localhost:8080/api/v0.1/client/get-clients-with-client-sites/
    @GetMapping("/get-clients-with-client-sites")
    public List<ClientWithClientSiteDto> getListOfClientsWithClientSites() {
        return clientService.getListOfClientsWithClientSites();
    }
}
