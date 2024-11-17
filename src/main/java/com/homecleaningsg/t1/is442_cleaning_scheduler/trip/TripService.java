package com.homecleaningsg.t1.is442_cleaning_scheduler.trip;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.routing.OdMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
@Service
public class TripService {

    private final TripRepository tripRepository;
    private final LocationRepository locationRepository;
    private final OdMatrixService odMatrixService;

    @Autowired
    public TripService(TripRepository tripRepository, LocationRepository locationRepository, OdMatrixService odMatrixService) {
        this.tripRepository = tripRepository;
        this.locationRepository = locationRepository;
        this.odMatrixService = odMatrixService;
    }

    @Transactional
    public synchronized void buildTrips(Location tripNode) {
        List<Location> otherLocations = locationRepository.findAll();
        List<Trip> newTrips = new ArrayList<>();
        for (Location otherLocation : otherLocations) {
            if (!tripNode.equals(otherLocation) && !tripRepository.existsByOriginAndDestination(tripNode, otherLocation)) {
                Trip fromTrip = new Trip(tripNode, otherLocation);
                route(fromTrip);
                Trip toTrip = new Trip(otherLocation, tripNode);
                route(toTrip);
                newTrips.add(fromTrip);
                newTrips.add(toTrip);
            }
        }
        tripRepository.saveAll(newTrips);
    }

    public Mono<Void> updateTripDistanceDurationAsync() {
        List<Trip> trips = tripRepository.findAllByTripDurationSecondsEquals(0.0);

        // Set trip duration to -1 to indicate that the trip duration is being updated
        trips.forEach(trip -> trip.setTripDurationSeconds(-1));
        tripRepository.saveAll(trips);

        return Flux.fromIterable(trips)
                .flatMap(trip -> odMatrixService.getOdMatrix(
                                        List.of(trip.getOrigin().getPostalCode()),
                                        List.of(trip.getDestination().getPostalCode())
                                )
                                .map(odMatrixResponse -> {
                                    if (!odMatrixResponse.getRows().isEmpty() && !odMatrixResponse.getRows().get(0).getElements().isEmpty()) {
                                        trip.setTripDurationSeconds(odMatrixResponse.getRows().get(0).getElements().get(0).getDuration().getValue());
                                        trip.setTripDistanceMeters(odMatrixResponse.getRows().get(0).getElements().get(0).getDistance().getValue());
                                    }
                                    return trip;
                                })
                )
                .collectList() // Collect all updated trips
                .flatMap(updatedTrips -> Mono.fromRunnable(() -> tripRepository.saveAll(updatedTrips))) // Save in one batch
                .then();
    }

    public void route(Trip trip) {
        odMatrixService.getOdMatrix(
                        List.of(trip.getOrigin().getPostalCode()),
                        List.of(trip.getDestination().getPostalCode())
                )
                .doOnNext(odMatrixResponse -> {
                    if (!odMatrixResponse.getRows().isEmpty() && !odMatrixResponse.getRows().get(0).getElements().isEmpty()) {
                        trip.setTripDurationSeconds(odMatrixResponse.getRows().get(0).getElements().get(0).getDuration().getValue());
                        trip.setTripDistanceMeters(odMatrixResponse.getRows().get(0).getElements().get(0).getDistance().getValue());
                    }
                })
                .block();  // Forces synchronous execution
    }
}
