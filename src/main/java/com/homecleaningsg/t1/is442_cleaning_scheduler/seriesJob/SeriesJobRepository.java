package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
//public interface SeriesJobRepository extends JpaRepository<SeriesJob, Integer> {
//}

import java.util.List;
import java.util.Optional;

@Repository
public interface SeriesJobRepository extends JpaRepository<SeriesJob, SeriesJobId> {
//    Optional<SeriesJob> findBySeries_SeriesId(int seriesId);
    List<SeriesJob> findBySeries_SeriesId(int seriesId);

    Optional<SeriesJob> findBySeries_SeriesIdAndJobId(int seriesId, int jobId);
}