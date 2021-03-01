package etu.demo.repository;

import etu.demo.domain.ERoles;
import etu.demo.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role , Long> {
    Role findByName(ERoles name);
}
