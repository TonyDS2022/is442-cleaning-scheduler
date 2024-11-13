package com.homecleaningsg.t1.is442_cleaning_scheduler.trip;

import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByEuclideanDistanceKmEquals(double euclideanDistanceKm);

    List<Trip> findAllByTripDurationSecondsEquals(double tripDurationSeconds);

    Trip findTripByOriginAndDestination(Location origin, Location destination);

    List<Trip> findAllByOriginOrDestination(Location tripNode, Location tripNode1);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByOriginAndDestination(Location origin, Location tripNode);
}
