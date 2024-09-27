// PackageJobController.java
package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v0.1/seriesJobs")
public class SeriesJobController {

    private final SeriesJobService packageJobService;

    @Autowired
    public SeriesJobController(SeriesJobService packageJobService) {
        this.packageJobService = packageJobService;
    }

    @GetMapping
    public List<SeriesJob> getPackageJobs() {
        return packageJobService.getPackageJobs();
    }
}