package com.homecleaningsg.t1.is442_cleaning_scheduler.contract;

import com.homecleaningsg.t1.is442_cleaning_scheduler.client.ClientSiteDto;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ContractListViewDto {
    Long contractId;
    String clientName;
    String clientPhone;
    ClientSiteDto clientSite;
    LocalDate contractStart;
    LocalDate contractEnd;
    Contract.Frequency frequency;
    int workersBudgeted;
    int numberOfSessions;
    Contract.ContractStatus contractStatus;

    public ContractListViewDto(Contract contract) {
        this.contractId = contract.getContractId();
        this.clientName = contract.getClient().getName();
        this.clientPhone = contract.getClient().getPhone();
        this.clientSite = new ClientSiteDto(contract.getClientSite());
        this.contractStart = contract.getContractStart();
        this.contractEnd = contract.getContractEnd();
        this.frequency = contract.getFrequency();
        this.workersBudgeted = contract.getWorkersBudgeted();
        this.numberOfSessions = contract.getCleaningSessions().size();
        this.contractStatus = contract.getContractStatus();
    }
}
