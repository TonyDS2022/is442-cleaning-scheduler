package com.homecleaningsg.t1.is442_cleaning_scheduler.devConfig;

import com.homecleaningsg.t1.is442_cleaning_scheduler.admin.AdminConfig;
import com.homecleaningsg.t1.is442_cleaning_scheduler.admin.AdminRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionConfig;
import com.homecleaningsg.t1.is442_cleaning_scheduler.cleaningSession.CleaningSessionRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractConfig;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.ContractRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationConfig;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.sessionTicket.SessionTicketConfig;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.SubzoneConfig;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.SubzoneRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripConfig;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerConfig;
import com.homecleaningsg.t1.is442_cleaning_scheduler.worker.WorkerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

// TODO: Move dev-specific configurations eventually to test
@Configuration
@ComponentScan(basePackages = "com.homecleaningsg.t1.is442_cleaning_scheduler")
public class DevelopmentConfig {

    @Bean
    public SubzoneConfig subzoneConfig(SubzoneRepository subzoneRepository) {
        return new SubzoneConfig(subzoneRepository);
    }

    @Bean
    @DependsOn("subzoneConfig")
    public LocationConfig locationConfig(LocationRepository locationRepository, LocationService locationService) {
        return new LocationConfig(locationRepository, locationService);
    }

    @Bean
    @DependsOn("locationConfig")
    public TripConfig tripConfig(TripService tripService) {
        return new TripConfig(tripService);
    }

    @Bean
    public WorkerConfig workerConfig(WorkerRepository workerRepository) {
        return new WorkerConfig(workerRepository);
    }

    @Bean
    @DependsOn("workerConfig")
    public ContractConfig contractConfig(ContractRepository contractRepository) {
        return new ContractConfig(contractRepository);
    }

    @Bean
    @DependsOn({"workerConfig", "contractConfig"})
    public CleaningSessionConfig cleaningSessionConfig(CleaningSessionRepository cleaningSessionRepository, ContractRepository contractRepository, WorkerRepository workerRepository) {
        return new CleaningSessionConfig(cleaningSessionRepository, contractRepository, workerRepository);
    }

    @Bean
    @DependsOn({"workerConfig", "contractConfig", "cleaningSessionConfig"})
    public SessionTicketConfig sessionTicketConfig() {
        return new SessionTicketConfig();
    }

    @Bean
    public AdminConfig adminConfig(AdminRepository adminRepository) {
        return new AdminConfig(adminRepository);
    }
}