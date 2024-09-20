// PackageJobService.java
package com.homecleaningsg.t1.is442_cleaning_scheduler.package_job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageJobService {

    private final PackageJobRepository packageJobRepository;

    @Autowired
    public PackageJobService(PackageJobRepository packageJobRepository) {
        this.packageJobRepository = packageJobRepository;
    }

    public List<PackageJob> getPackageJobs() {
        return packageJobRepository.findAll();
    }
}