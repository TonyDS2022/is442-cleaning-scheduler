package com.homecleaningsg.t1.is442_cleaning_scheduler.subzone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubzoneService {

    private final SubzoneRepository subzoneRepository;

    @Autowired
    public SubzoneService(SubzoneRepository subzoneRepository) {
        this.subzoneRepository = subzoneRepository;
    }

    public List<Subzone> getAllSubzones() {
        return subzoneRepository.findAll();
    }
}
