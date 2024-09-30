package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeriesJobService {

    private final SeriesJobRepository seriesJobRepository;

    @Autowired
    public SeriesJobService(SeriesJobRepository seriesJobRepository) {
        this.seriesJobRepository = seriesJobRepository;
    }

    public List<SeriesJob> getAllSeriesJobs() {
        return seriesJobRepository.findAll();
    }

    public List<SeriesJob> getSeriesJobBySeriesId(int seriesId) {
        return seriesJobRepository.findBySeries_SeriesId(seriesId);
    }

    public Optional<SeriesJob> getSeriesJobBySeriesIdAndJobId(int seriesId, int jobId) {
        return seriesJobRepository.findBySeries_SeriesIdAndJobId(seriesId, jobId);
    }
}