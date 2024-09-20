package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.Subzone;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.SubzoneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class LocationConfig {

    @Bean
    @Order(2)
    CommandLineRunner locationCommandLineRunner(LocationRepository locationRepository, SubzoneRepository subzoneRepository) {
        return args -> {
            // Disclaimer: These are DEFINITELY random addresses and NOT past residential
            // addresses of the author.
            //Subzone subzone1 = subzoneRepository.findSubzoneBySubzoneName("TAMAN JURONG");
            //Subzone subzone2 = subzoneRepository.findSubzoneBySubzoneName("TANJONG RHU");
            Location loc1 = new Location("649823", "88 Corporation Road");
            Location loc2 = new Location("438181", "61 Kampong Arang Road");

            loc1.setLatitude(1.3428337164417088);
            loc1.setLongitude(103.71649893878133);
            Subzone subzone1 = subzoneRepository.findSubzoneByLatLong(loc1.getLatitude(), loc1.getLongitude());
            loc1.setSubzone(subzone1);

            loc2.setLatitude(1.299823341971301);
            loc2.setLongitude(103.88234245412214);
            Subzone subzone2 = subzoneRepository.findSubzoneByLatLong(loc2.getLatitude(), loc2.getLongitude());
            loc2.setSubzone(subzone2);

            locationRepository.saveAll(List.of(loc1, loc2));
        };
    }
}
