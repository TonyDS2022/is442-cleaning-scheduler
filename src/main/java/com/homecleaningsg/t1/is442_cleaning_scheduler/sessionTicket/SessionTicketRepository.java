package com.homecleaningsg.t1.is442_cleaning_scheduler.sessionTicket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionTicketRepository extends JpaRepository<SessionTicket, Integer> {
    List<SessionTicket> findByWorkerWorkerId(Long workerId);
}