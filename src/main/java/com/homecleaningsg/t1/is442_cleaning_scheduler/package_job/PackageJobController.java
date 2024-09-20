// PackageJobController.java
package com.homecleaningsg.t1.is442_cleaning_scheduler.package_job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v0.1/packageJobs")
public class PackageJobController {

    private final PackageJobService packageJobService;

    @Autowired
    public PackageJobController(PackageJobService packageJobService) {
        this.packageJobService = packageJobService;
    }

    @GetMapping
    public List<PackageJob> getPackageJobs() {
        return packageJobService.getPackageJobs();
    }
}