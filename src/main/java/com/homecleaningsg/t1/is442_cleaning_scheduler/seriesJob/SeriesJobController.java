package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v0.1/seriesJob")
public class SeriesJobController {

    private final SeriesJobService seriesJobService;

    @Autowired
    public SeriesJobController(SeriesJobService seriesJobService) {
        this.seriesJobService = seriesJobService;
    }

    @GetMapping
    public List<SeriesJob> getAllSeriesJobs() {
        return seriesJobService.getAllSeriesJobs();
    }

    @GetMapping("/{seriesId}")
    public List<SeriesJob> getSeriesJobBySeriesId(@PathVariable int seriesId) {
        return seriesJobService.getSeriesJobBySeriesId(seriesId);
    }

    @GetMapping("/{seriesId}/{jobId}")
    public Optional<SeriesJob> getSeriesJobBySeriesIdAndJobId(@PathVariable int seriesId, @PathVariable int jobId) {
        return seriesJobService.getSeriesJobBySeriesIdAndJobId(seriesId, jobId);
    }
}