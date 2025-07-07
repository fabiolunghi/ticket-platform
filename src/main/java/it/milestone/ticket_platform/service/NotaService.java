package it.milestone.ticket_platform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.milestone.ticket_platform.model.Nota;
import it.milestone.ticket_platform.model.Ticket;
import it.milestone.ticket_platform.repository.NotaRepository;

@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    public Nota save (Nota nota){
        return notaRepository.save(nota);
    }

    public List<Nota> findByTicket(Ticket ticket) {
        return notaRepository.findByTicket(ticket);
    }

    public void deleteById(Long id) {
        notaRepository.deleteById(id);
    }
}
