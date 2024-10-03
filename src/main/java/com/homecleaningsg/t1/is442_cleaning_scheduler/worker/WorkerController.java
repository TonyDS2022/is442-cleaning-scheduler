package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v0.1/workers")
public class WorkerController {
    private final WorkerService workerService;

    @Autowired
    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping
    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }


    @GetMapping("/{id}")
    public Worker getWorkerById(@PathVariable("id") long id) {
        return workerService.getWorkerById(id);
    }
}
