package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import com.homecleaningsg.t1.is442_cleaning_scheduler.geocoding.GeocodingService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.SubzoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final GeocodingService geocodingService;
    private final SubzoneRepository subzoneRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository, GeocodingService geocodingService, SubzoneRepository subzoneRepository) {
        this.locationRepository = locationRepository;
        this.geocodingService = geocodingService;
        this.subzoneRepository = subzoneRepository;
    }

    public List<Location> getLocations() {
        return locationRepository.findAll();
    }

    public List<Location> getLocationsWithNoCoordinates() {
        return locationRepository.findByLatitudeIsNullAndLongitudeIsNull();
    }

    public Mono<Void> updateLocationLatLong() {
        List<Location> locations = getLocationsWithNoCoordinates();
        return Flux.fromIterable(locations)
                .flatMap(location -> geocodingService.getCoordinates(location.getPostalCode())
                        .doOnNext(geocodingResult -> {
                            location.setLatitude(geocodingResult.getLatitude());
                            location.setLongitude(geocodingResult.getLongitude());
                            location.setSubzone(subzoneRepository);
                            locationRepository.save(location);
                        }))
                .then();
    }

    public Location getOrCreateLocation(String postalCode) {
        return locationRepository.findByPostalCode(postalCode)
                .orElseGet(() -> {
                    Location location = new Location();
                    location.setPostalCode(postalCode);
                    return locationRepository.save(location);
                });
    }

    public Location geoCode(Location location) {
        return geocodingService.getCoordinates(location.getPostalCode())
                .map(geocodingResult -> {
                    location.setLatitude(geocodingResult.getLatitude());
                    location.setLongitude(geocodingResult.getLongitude());
                    return locationRepository.save(location);
                }).block();
    }
}
