package com.homecleaningsg.t1.is442_cleaning_scheduler.devConfig;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationConfig;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.SubzoneConfig;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.SubzoneRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

// TODO: Move dev-specific configurations eventually to test
@Configuration
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

}
