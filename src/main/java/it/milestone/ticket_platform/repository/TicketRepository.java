package it.milestone.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.ticket_platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Cerca ticket per stato (es: "DA FARE", "IN CORSO", "COMPLETATO")
    List<Ticket> findByStatoIgnoreCase(String stato);

    // Cerca ticket per categoria
    List<Ticket> findByCategoriaNomeContainingIgnoreCase(String nome);

    // Cerca ticket per testo nel titolo
    List<Ticket> findByTitoloContainingIgnoreCase(String titolo);

}
