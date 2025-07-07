package it.milestone.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.ticket_platform.model.Nota;
import it.milestone.ticket_platform.model.Ticket;

public interface NotaRepository extends JpaRepository<Nota, Long>{

    //Visualizza le note per ogni ticket
    List<Nota> findByTicket (Ticket ticket);

}
