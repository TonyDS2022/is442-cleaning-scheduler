package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesJobRepository extends JpaRepository<SeriesJob, Integer> {
}