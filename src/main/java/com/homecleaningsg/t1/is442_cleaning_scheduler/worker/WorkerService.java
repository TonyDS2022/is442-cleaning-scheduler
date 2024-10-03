package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerService {
    private final WorkerRepository WorkerRepository;

    @Autowired
    public WorkerService(WorkerRepository workerRepository) {
        this.WorkerRepository = workerRepository;
    }

    public List<Worker> getAllWorkers() {
        return WorkerRepository.findAll();
    }

    public Worker getWorkerById(long id) {
        return WorkerRepository.findById(id).orElse(null);
    }
}
