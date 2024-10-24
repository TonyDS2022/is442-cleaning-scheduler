package com.homecleaningsg.t1.is442_cleaning_scheduler.trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByEuclideanDistanceKmEquals(double euclideanDistanceKm);

    List<Trip> findAllByTripDurationSecondsEquals(double tripDurationSeconds);
}
