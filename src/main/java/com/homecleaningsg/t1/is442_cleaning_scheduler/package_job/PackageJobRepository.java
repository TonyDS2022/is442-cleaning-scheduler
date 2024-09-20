// PackageJobRepository.java
package com.homecleaningsg.t1.is442_cleaning_scheduler.package_job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageJobRepository extends JpaRepository<PackageJob, Integer> {
}