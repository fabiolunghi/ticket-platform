package it.milestone.ticket_platform.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.milestone.ticket_platform.model.Nota;
import it.milestone.ticket_platform.model.Ticket;
import it.milestone.ticket_platform.model.User;
import it.milestone.ticket_platform.service.NotaService;
import it.milestone.ticket_platform.service.TicketService;
import it.milestone.ticket_platform.service.UserService;

@Controller
@RequestMapping("/operatore")
public class OperatoreController {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private NotaService notaService;

    // 1. Lista dei ticket assegnati all'operatore loggato
    @GetMapping("/tickets")
    public String ticketsAssegnati(Model model, Principal principal) {
        User operatore = userService.findByUsername(principal.getName());
        List<Ticket> tickets = operatore.getTickets();
        model.addAttribute("tickets", tickets);
        return "/admin/tickets";
    }

    // 2. Dettaglio di un ticket assegnato
    @GetMapping("/tickets/{id}")
    public String dettaglioTicket(@PathVariable Long id, Model model, Principal principal) {
        User operatore = userService.findByUsername(principal.getName());
        Ticket ticket = ticketService.findById(id);
        //Controllo di verifica, il ticket deve essere dell'operatore loggato
        if (!ticket.getUtente().getId().equals(operatore.getId())) {
            return "redirect:/admin/tickets";
        }

        model.addAttribute("ticket", ticket);
        model.addAttribute("note", notaService.findByTicket(ticket));
        model.addAttribute("nuovaNota", new Nota());

        return "admin/dettaglioTicket";
    }

    // 3. Cambia stato del ticket
    @PostMapping("/tickets/{id}/stato")
    public String aggiornaStatoTicket(@PathVariable Long id,
                                      @RequestParam("stato") String nuovoStato,
                                      Principal principal, RedirectAttributes redirectAttributes) {
        User operatore = userService.findByUsername(principal.getName());
        Ticket ticket = ticketService.findById(id);

        if (ticket.getUtente().getId().equals(operatore.getId())) {
            ticket.setStato(nuovoStato);
            ticketService.saveTicket(ticket);
        }

        return "redirect:/operatore/tickets";
    }

    // 4. Aggiungi nota
    @PostMapping("/tickets/{id}/note")
    public String aggiungiNota(@PathVariable Long id,
                               @RequestParam("testo") String testo,
                               Principal principal) {
        User autore = userService.findByUsername(principal.getName());
        Ticket ticket = ticketService.findById(id);

        Nota nota = new Nota();
        nota.setTesto(testo);
        nota.setTicket(ticket);
        nota.setAutore(autore);
        nota.setDataCreazione(LocalDateTime.now());
        notaService.save(nota);

        return "redirect:/operatore/tickets/" + id;
    }

    //Elimina un nota
    @PostMapping("/tickets/{ticketId}/note/{notaId}/delete")
    public String eliminaNotaOperatore(@PathVariable Long ticketId,
                                   @PathVariable Long notaId,
                                   Principal principal,
                                   RedirectAttributes ra) {
    notaService.deleteById(notaId);
    ra.addFlashAttribute("successMessage", "Nota eliminata con successo!");
    return "redirect:/operatore/tickets/" + ticketId;
}



    // Cambia stato personale (solo se nessun ticket attivo)
    @PostMapping("/profilo/stato")
    public String cambiaStatoPersonale(
        @RequestParam("stato") boolean nuovoStato,
        Principal principal,
        RedirectAttributes ra) {

    User operatore = userService.findByUsername(principal.getName());
    //Verifico che non ci siano ticket attivi
    if (!nuovoStato) {
        boolean haTicketAttivi = operatore.getTickets().stream()
            .anyMatch(t -> t.getStato().equalsIgnoreCase("DA FARE")
                        || t.getStato().equalsIgnoreCase("IN CORSO"));
        if (haTicketAttivi) {
            ra.addFlashAttribute("errorMessage",
                "Non puoi diventare non disponibile finché hai ticket da fare o in corso.");
            return "redirect:/operatore/profilo";
        }
    }

    operatore.setStato(nuovoStato);
    userService.save(operatore);

    ra.addFlashAttribute("successMessage", "Stato aggiornato con successo ");
    return "redirect:/operatore/profilo";
}


    // Mostra la pagina profilo
    @GetMapping("/profilo")
    public String profilo(Model model, Principal principal) {
        User operatore = userService.findByUsername(principal.getName());
        model.addAttribute("utente", operatore);
        return "operatore/profilo";
    }
}

