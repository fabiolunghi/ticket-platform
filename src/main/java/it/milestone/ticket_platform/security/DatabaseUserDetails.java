package it.milestone.ticket_platform.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.milestone.ticket_platform.model.Role;
import it.milestone.ticket_platform.model.User;

public class DatabaseUserDetails implements UserDetails {

    private final Long id; 
    private final String username; 
    private final String password; 
    private final List<GrantedAuthority> authorities; 

    public DatabaseUserDetails(User user) { 
        this.id = user.getId(); 
        this.username = user.getUsername(); 
        this.password = user.getPassword();    
        this.authorities = new ArrayList<>();

        Role role = user.getRuolo();
        this.authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getNome()));
    }

    @Override
    public String getUsername(){
        return this.username;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

}
