package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByLatitudeIsNullAndLongitudeIsNull();

    @Query("SELECT l FROM Location l WHERE l.postalCode = :postalCode")
    Location getLocationByPostalCode(String postalCode);
}
