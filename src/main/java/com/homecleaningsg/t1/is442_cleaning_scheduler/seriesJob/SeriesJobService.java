// PackageJobService.java
package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesJobService {

    private final SeriesJobRepository packageJobRepository;

    @Autowired
    public SeriesJobService(SeriesJobRepository packageJobRepository) {
        this.packageJobRepository = packageJobRepository;
    }

    public List<SeriesJob> getPackageJobs() {
        return packageJobRepository.findAll();
    }
}