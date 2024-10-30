package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

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

    @GetMapping
    public List<Client> getAllClient() {
        return clientService.getAllClients();
    }


    @GetMapping("/{name}")
    public Client getClientByName(@PathVariable("name") String name) {
        return clientService.getClientByName(name);
    }


    @PostMapping("/add-client/")
    public ResponseEntity<String> addClient(@RequestBody Client client) {
        try {
            clientService.addClient(client);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Client added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add client.");
        }
    }

    @PutMapping("/update-client/{clientId}")
    public ResponseEntity<String> updateClient(
            @PathVariable("clientId") Long clientId, @RequestBody Client updatedClient) {
        try {
            clientService.updateClient(clientId, updatedClient);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Client updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update client details.");
        }
    }

    // localhost:8080/api/v0.1/client/deactivate-client/1
    @PutMapping("/deactivate-client/{clientId}")
    public ResponseEntity<String> deactivateClient(@PathVariable("clientId") Long clientId) {
        try {
            clientService.deactivateClient(clientId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Client deactivated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to deactivate client.");
        }
    }
}
