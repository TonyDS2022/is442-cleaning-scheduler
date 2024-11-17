package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import com.homecleaningsg.t1.is442_cleaning_scheduler.geocoding.GeocodingService;
import com.homecleaningsg.t1.is442_cleaning_scheduler.subzone.SubzoneRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final GeocodingService geocodingService;
    private final SubzoneRepository subzoneRepository;
    private final TripRepository tripRepository;
    private final TripService tripService;

    @Autowired
    public LocationService(LocationRepository locationRepository, GeocodingService geocodingService, SubzoneRepository subzoneRepository, TripRepository tripRepository, TripService tripService) {
        this.locationRepository = locationRepository;
        this.geocodingService = geocodingService;
        this.subzoneRepository = subzoneRepository;
        this.tripRepository = tripRepository;
        this.tripService = tripService;
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

    @Transactional
    public synchronized Location getOrCreateLocation(String postalCode, String address) {
        // @Param postalCode: Postal Code of the location
        // @Param address: Address of the location
        // Queries locationRepository to find a location by postalCode
        // If location is found, return the location
        // If location is not found, create a new location with the postalCode and address
        // Call geoCode to get the latitude and longitude of the location
        // Set the subzone of the location
        // Call tripService to build trips for the location
        // Call tripService to update the trip duration by calling Google Distance Matrix API
        // return the existing or new location
        Location location = locationRepository.getLocationByPostalCode(postalCode);

        if (location != null) {
            return location;
        }
        Location newLocation = new Location(address, postalCode);
        newLocation = geoCode(newLocation);
        newLocation.setSubzone(subzoneRepository);
        tripService.buildTrips(newLocation);
        // update Trip Duration by calling Google Distance Matrix API
        tripService.updateTripDistanceDurationAsync().subscribe();
        // sleep for 1 second to avoid Google Distance Matrix API rate limit
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return locationRepository.save(newLocation);
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
