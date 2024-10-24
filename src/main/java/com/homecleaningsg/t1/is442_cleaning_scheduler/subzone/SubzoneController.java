package com.homecleaningsg.t1.is442_cleaning_scheduler.subzone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v0.1/subzones")
public class SubzoneController {

    private final SubzoneService subzoneService;

    @Autowired
    public SubzoneController(SubzoneService subzoneService) {
        this.subzoneService = subzoneService;
    }

    @GetMapping
    public List<Subzone> getSubzones() {
        return subzoneService.getAllSubzones();
    }
}
