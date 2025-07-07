package it.milestone.ticket_platform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.milestone.ticket_platform.model.Ticket;
import it.milestone.ticket_platform.repository.TicketRepository;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket findById(Long id){
        return ticketRepository.findById(id).orElse(null);
    }

    public Ticket saveTicket (Ticket ticket){
        return ticketRepository.save(ticket);
    }

    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> findByStato(String stato) {
        return ticketRepository.findByStatoIgnoreCase(stato);
    }

    public List<Ticket> findByCategoriaNome(String nomeCategoria) {
        return ticketRepository.findByCategoriaNomeContainingIgnoreCase(nomeCategoria);
    }

    public List<Ticket> cercaPerTitolo(String testo) {
        return ticketRepository.findByTitoloContainingIgnoreCase(testo);
    }

    public void aggiornaStato(Long ticketId, String nuovoStato) {
    Ticket ticket = ticketRepository.findById(ticketId).orElse(null);

    if (ticket != null) {
        ticket.setStato(nuovoStato);
        ticketRepository.save(ticket);
        }
    }

}
