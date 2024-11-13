package com.homecleaningsg.t1.is442_cleaning_scheduler.client;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientWithClientSiteDto {
    private Long clientId;
    private String name;
    private List<ClientSiteDto> listOfClientSiteDto;
}
