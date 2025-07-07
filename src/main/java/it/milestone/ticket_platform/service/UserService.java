package it.milestone.ticket_platform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.milestone.ticket_platform.model.User;
import it.milestone.ticket_platform.repository.UserRepository;

@Service

public class UserService {

    @Autowired
    private UserRepository userRepository;

    //Filtra per id
    public User findById(Long id) {
    User user = userRepository.findById(id).orElse(null);
    return user;
    }

    //Filtra per email
    public User findByEmail(String email) {
    return userRepository.findByEmail(email);
    }

    //Filtra per username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).
            orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));
    }


    //Aggiorna lo stato dell'operatore
    public void aggiornaStato(Long userId, boolean nuovoStato) {
    User user = userRepository.findById(userId).orElse(null);

    if (user != null) {
        user.setStato(nuovoStato);
        userRepository.save(user);
        }
    }

    //Visualizza tutti gli operatori
    public List<User> getOperatori() {
    return userRepository.findAll().stream()
            .filter(u -> u.getRuolo().getNome().equals("OPERATORE"))
            .collect(Collectors.toList());
    }

    //Trova tutti gli operatori disponibili
    public List<User> getOperatoriDisponibili() {
    List<User> tuttiGliUtenti = userRepository.findAll();
    List<User> operatoriDisponibili = new ArrayList<>();

    for (User user : tuttiGliUtenti) {
        // Controlla se ha ruolo OPERATORE e stato = true (disponibile)
        if (user.getRuolo() != null &&
            user.getRuolo().getNome().equalsIgnoreCase("OPERATORE") &&
            Boolean.TRUE.equals(user.getStato())) {
            
            operatoriDisponibili.add(user);
        }
    }

    return operatoriDisponibili;
    }

    //Salva lo stato di un operatore
    public User save(User user) {
    return userRepository.save(user);
    }


}
