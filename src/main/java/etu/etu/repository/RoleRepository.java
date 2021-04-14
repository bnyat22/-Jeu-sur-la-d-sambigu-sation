package etu.etu.repository;

import etu.etu.domain.ERoles;
import etu.etu.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role , Long> {
    Role findByName(ERoles name);
}
