package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import com.homecleaningsg.t1.is442_cleaning_scheduler.clientSite.ClientSite;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ClientSiteDto {
    private String streetAddress;
    private String postalCode;
    private String uniNumber;
    private Long numberOfRooms;
    private ClientSite.PropertyType propertyType;

    public ClientSiteDto(ClientSite clientSite) {
        this.streetAddress = clientSite.getStreetAddress();
        this.postalCode = clientSite.getPostalCode();
        this.uniNumber = clientSite.getUnitNumber();
        this.numberOfRooms = clientSite.getNumberOfRooms();
        this.propertyType = clientSite.getPropertyType();
    }
}