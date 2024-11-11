package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSiteRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
public class ClientConfig implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final LocationRepository locationRepository;
    private final ClientSiteRepository clientSiteRepository;

    public ClientConfig(ClientRepository clientRepository,
                        LocationRepository locationRepository,
                        ClientSiteRepository clientSiteRepository) {
        this.clientRepository = clientRepository;
        this.locationRepository = locationRepository;
        this.clientSiteRepository = clientSiteRepository;

        Location location1 = this.locationRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Location with ID 1 not found"));
        Location location2 = this.locationRepository.findById(2L).orElseThrow(() -> new IllegalStateException("Location with ID 2 not found"));

        Client client1 = new Client("Amy Santiago", "98472094", true,  LocalDate.of(2024,10,4));
        Client client2 = new Client("Jake Peralta", "92384923", true, LocalDate.of(2024,8,2));
        this.clientRepository.saveAll(List.of(client1, client2));

        ClientSite clientSite1 = new ClientSite(client1, location1.getAddress(), location1.getPostalCode(), "#01-01", location1);
        ClientSite clientSite2 = new ClientSite(client2, location2.getAddress(), location2.getPostalCode(), "#02-02", location2);

        client1.setDeactivatedAt(LocalDate.of(2024, 11, 4));
        client1.setActive(false);
        this.clientSiteRepository.saveAll(List.of(clientSite1, clientSite2));

    }
    @Override
    public void run(String... args) throws Exception {
    }
}
