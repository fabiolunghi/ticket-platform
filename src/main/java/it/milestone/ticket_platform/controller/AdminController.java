package it.milestone.ticket_platform.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.milestone.ticket_platform.model.Nota;
import it.milestone.ticket_platform.model.Ticket;
import it.milestone.ticket_platform.model.User;
import it.milestone.ticket_platform.service.CategoriaService;
import it.milestone.ticket_platform.service.NotaService;
import it.milestone.ticket_platform.service.TicketService;
import it.milestone.ticket_platform.service.UserService;

@Controller
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotaService notaService;

    @Autowired
    private CategoriaService categoriaService;

    //Lista dei ticket
    @GetMapping("/tickets")
    public String listaTickets(@RequestParam(required = false) String titolo, Model model) {
    List<Ticket> tickets = (titolo != null && !titolo.isBlank())
        ? ticketService.cercaPerTitolo(titolo)
        : ticketService.findAll();

    model.addAttribute("tickets", tickets);
    return "admin/tickets";
    }

    //Creazione di un nuovo ticket
    @GetMapping("/tickets/nuovoTicket")
    public String nuovoTicket(Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("categorie", categoriaService.findAll());
        model.addAttribute("operatori", userService.getOperatoriDisponibili());
        return "admin/nuovoTicket";
    }

    //Salvataggio di un nuovo ticket
    @PostMapping("/tickets/nuovoTicket")
    public String salvaNuovoTicket(@ModelAttribute Ticket ticket) {
        ticket.setDataCreazione(LocalDateTime.now());
        ticketService.saveTicket(ticket);
        return "redirect:/admin/tickets";
    }

     

    //Modifica di un ticket esistente
    @GetMapping("/tickets/modifica/{id}")
    public String modificaTicketForm(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.findById(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("categorie", categoriaService.findAll());
        model.addAttribute("operatori", userService.getOperatoriDisponibili());
        return "admin/modificaTicket";
    }

    //Salvataggio della modifica del ticket
    @PostMapping("/tickets/modifica/{id}")
    public String salvaModificaTicket(@PathVariable Long id,
                                    @ModelAttribute Ticket ticket,
                                    RedirectAttributes redirectAttributes) {

    Ticket esistente = ticketService.findById(id);
    ticket.setDataCreazione(esistente.getDataCreazione());
    ticketService.saveTicket(ticket);
    redirectAttributes.addFlashAttribute("successMessage", "Ticket modificato con successo");

    return "redirect:/admin/tickets";
}

    //Dettaglio del ticket
    @GetMapping("/tickets/{id}")
    public String show (@PathVariable Long id , Model model) {

        Ticket ticket = ticketService.findById(id);

        model.addAttribute("ticket", ticket);
        model.addAttribute("note", notaService.findByTicket(ticket));
        model.addAttribute("nuovaNota", new Nota());
        return "admin/dettaglioTicket";
    }

    //Aggiunta e salvataggio di una nota ad un ticket
    @PostMapping("/tickets/{id}/note")
    public String aggiungiNota(@PathVariable Long id,@ModelAttribute("nuovaNota") Nota nota,
                               Principal principal) {

        Ticket ticket = ticketService.findById(id);
        User autore = userService.findByUsername(principal.getName());

        nota.setId(null);
        nota.setAutore(autore);
        nota.setTicket(ticket);
        nota.setDataCreazione(LocalDateTime.now());

        notaService.save(nota);

        return "redirect:/admin/tickets/" + id;
    }

    //Eliminazione di una nota
    @PostMapping("/tickets/{ticketId}/note/{notaId}/delete")
    public String eliminaNotaAdmin(@PathVariable Long ticketId,
                               @PathVariable Long notaId,
                               RedirectAttributes ra) {
    notaService.deleteById(notaId);
    ra.addFlashAttribute("successMessage", "Nota eliminata con successo!");
    return "redirect:/admin/tickets/" + ticketId;
}


    //Elimina un ticket
    @PostMapping("/tickets/{id}/delete")
    public String eliminaTicket(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
    ticketService.delete(id);
    redirectAttributes.addFlashAttribute("successMessage",
                                          "Ticket #" + id + " eliminato con successo!");

        return "redirect:/admin/tickets";
    }


    //Restituzione la lista di operatori
    @GetMapping("/operatori")
    public String listaOperatori(Model model) {

        List<User> operatori = userService.getOperatori();
        model.addAttribute("operatori", operatori);

    return "admin/operatori";
}

}
