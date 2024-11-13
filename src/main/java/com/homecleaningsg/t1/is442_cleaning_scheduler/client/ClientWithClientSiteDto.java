package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ClientWithClientSiteDto {

    private Long clientId;
    private String clientName;
    private String clientPhone;
    private List<ClientSiteDto> clientSites = new ArrayList<>();

    public ClientWithClientSiteDto(Client client) {
        this.clientId = client.getClientId();
        this.clientName = client.getName();
        this.clientPhone = client.getPhone();
        for (ClientSite clientSite : client.getClientSites()) {
            this.clientSites.add(new ClientSiteDto(clientSite));
        }
    }
}
