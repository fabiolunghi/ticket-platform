package it.milestone.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.milestone.ticket_platform.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
