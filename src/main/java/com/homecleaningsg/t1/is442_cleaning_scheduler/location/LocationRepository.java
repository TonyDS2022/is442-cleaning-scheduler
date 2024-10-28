package com.homecleaningsg.t1.is442_cleaning_scheduler.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByLatitudeIsNullAndLongitudeIsNull();
    Optional<Location> findByPostalCode(String postalCode);
}
