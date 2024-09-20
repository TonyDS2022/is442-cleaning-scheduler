package com.homecleaningsg.t1.is442_cleaning_scheduler.subzone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubzoneRepository extends JpaRepository<Subzone, Long> {
    public Subzone findSubzoneBySubzoneName(String subzoneName);

    @Query(value = "SELECT * FROM subzone s WHERE ST_Contains(s.subzone_geometry, ST_SetSRID(ST_MakePoint(CAST(:longitude AS double precision), CAST(:latitude AS double precision)), 4326))", nativeQuery = true)
    Subzone findSubzoneByLatLong(@Param("latitude") double latitude, @Param("longitude") double longitude);
}
