package com.homecleaningsg.t1.is442_cleaning_scheduler.trip;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.LocationRepository;
import com.homecleaningsg.t1.is442_cleaning_scheduler.routing.OdMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public List<Trip> getTrips() {
        updateTripEuclideanDistances();
        updateTripDistanceDurationAsync().block();
        return tripRepository.findAll();
    }

    public void buildTrips() {
        List<Location> locations = locationRepository.findAll();
        for (Location origin : locations) {
            for (Location destination : locations) {
                if (!origin.equals(destination)) {
                    Trip trip = new Trip();
                    trip.setOrigin(origin);
                    trip.setDestination(destination);
                    tripRepository.save(trip);
                }
            }
        }
    }

    public void updateTripEuclideanDistances() {
        List<Trip> unlabeledTrips = tripRepository.findAllByEuclideanDistanceKmEquals(0.0);
        for (Trip trip : unlabeledTrips) {
            trip.setEuclideanDistanceKm();
            tripRepository.save(trip);
        }
    }

    public Mono<Void> updateTripDistanceDurationAsync() {
        List<Trip> trips = tripRepository.findAllByTripDurationSecondsEquals(0.0);
        // Call OdMatrixService to get trip duration
        return Flux.fromIterable(trips)
                .flatMap(trip -> odMatrixService.getOdMatrix(List.of(trip.getOrigin().getPostalCode()), List.of(trip.getDestination().getPostalCode()))
                        .doOnNext(odMatrixResponse -> {
                            if (!odMatrixResponse.getRows().isEmpty() && !odMatrixResponse.getRows().get(0).getElements().isEmpty()) {
                                trip.setTripDurationSeconds(odMatrixResponse.getRows().get(0).getElements().get(0).getDuration().getValue());
                                trip.setTripDistanceMeters(odMatrixResponse.getRows().get(0).getElements().get(0).getDistance().getValue());
                                tripRepository.save(trip);
                            }
                        }))
                .then();
    }
}
