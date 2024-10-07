package com.homecleaningsg.t1.is442_cleaning_scheduler.sessionTicket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionTicketService {

    private final SessionTicketRepository sessionTicketRepository;

    @Autowired
    public SessionTicketService(SessionTicketRepository sessionTicketRepository) {
        this.sessionTicketRepository = sessionTicketRepository;
    }

    public List<SessionTicket> getAllSessionTickets() {
        return sessionTicketRepository.findAll();
    }

    public Optional<SessionTicket> getSessionTicketById(int id) {
        return sessionTicketRepository.findById(id);
    }

    public void addSessionTicket(SessionTicket sessionTicket) {
        sessionTicketRepository.save(sessionTicket);
    }

    public void updateSessionTicket(int id, SessionTicket sessionTicket) {
        if (sessionTicketRepository.existsById(id)) {
            sessionTicket.setSessionTicketId(id);
            sessionTicketRepository.save(sessionTicket);
        }
    }

    public void deleteSessionTicket(int id) {
        sessionTicketRepository.deleteById(id);
    }

    public List<SessionTicket> getSessionTicketsByWorkerId(Long workerId) {
        return sessionTicketRepository.findByWorkerWorkerId(workerId);
    }
}