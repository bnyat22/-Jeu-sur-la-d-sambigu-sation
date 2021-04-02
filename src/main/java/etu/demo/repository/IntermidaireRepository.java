package etu.demo.repository;

import etu.demo.domain.Intermédiaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntermidaireRepository extends JpaRepository<Intermédiaire , Long> {
    Intermédiaire getById(long id);
}
