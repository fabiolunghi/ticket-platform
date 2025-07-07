package it.milestone.ticket_platform.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.milestone.ticket_platform.model.Ticket;
import it.milestone.ticket_platform.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketRestController {

    @Autowired
    public TicketService ticketService;

    //Restituisce tutti i ticket
    @GetMapping
    public List<Ticket> allTickets (@RequestParam(name="keyword", required=false) String keyword){
        
        return ticketService.findAll();
    }

    //Visualizza l'elenco dei ticket per categoria
    @GetMapping("/category")
    public List<Ticket> getTickets(@RequestParam(name="category", required=false) String nomeCategoria ) {
    if (nomeCategoria == null || nomeCategoria.isBlank()) {
        return ticketService.findAll();
    }
    return ticketService.findByCategoriaNome(nomeCategoria);
    }


    //Visualizza l'elenco dei ticket per stato
    @GetMapping("/stato")
    public List<Ticket> statoTicket (@RequestParam(name="stato", required=false) String stato){
        if (stato == null || stato.isBlank()){
            return ticketService.findAll();
        }
        return ticketService.findByStato(stato);
    }

}
