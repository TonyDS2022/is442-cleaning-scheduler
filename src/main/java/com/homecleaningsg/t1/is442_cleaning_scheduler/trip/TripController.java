package com.homecleaningsg.t1.is442_cleaning_scheduler.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v0.1/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) { this.tripService = tripService; }

    @GetMapping
    public List<Trip> getTrips() {
        return tripService.getTrips();
    }
}
