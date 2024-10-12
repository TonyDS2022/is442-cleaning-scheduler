package com.homecleaningsg.t1.is442_cleaning_scheduler.sessionTicket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v0.1/sessionTicket")
public class SessionTicketController {

    private final SessionTicketService sessionTicketService;

    @Autowired
    public SessionTicketController(SessionTicketService sessionTicketService) {
        this.sessionTicketService = sessionTicketService;
    }

    @GetMapping
    public List<SessionTicket> getAllSessionTickets() {
        return sessionTicketService.getAllSessionTickets();
    }

    @GetMapping("/{sessionTicketId}")
    public Optional<SessionTicket> getSessionTicketById(@PathVariable("sessionTicketId") int sessionTicketId) {
        return sessionTicketService.getSessionTicketById(sessionTicketId);
    }

    @PostMapping
    public void addSessionTicket(@RequestBody SessionTicket sessionTicket) {
        sessionTicketService.addSessionTicket(sessionTicket);
    }

    @PutMapping("/{sessionTicketId}")
    public void updateSessionTicket(@PathVariable("sessionTicketId") int sessionTicketId, @RequestBody SessionTicket sessionTicket) {
        sessionTicketService.updateSessionTicket(sessionTicketId, sessionTicket);
    }

    @DeleteMapping("/{sessionTicketId}")
    public void deleteSessionTicket(@PathVariable("sessionTicketId") int sessionTicketId) {
        sessionTicketService.deleteSessionTicket(sessionTicketId);
    }

    @GetMapping("/worker/{workerId}")
    public List<SessionTicket> getSessionTicketsByWorkerId(@PathVariable("workerId") Long workerId) {
        return sessionTicketService.getSessionTicketsByWorkerId(workerId);
    }
}