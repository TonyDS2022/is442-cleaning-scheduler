package com.homecleaningsg.t1.is442_cleaning_scheduler.subzone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubzoneRepository extends JpaRepository<Subzone, Long> {
    public Subzone findSubzoneBySubzoneName(String subzoneName);
}
