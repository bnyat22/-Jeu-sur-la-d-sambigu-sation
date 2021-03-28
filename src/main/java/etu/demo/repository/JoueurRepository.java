package etu.demo.repository;

import etu.demo.domain.Expert;
import etu.demo.domain.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoueurRepository extends JpaRepository<Joueur , Long> {
Joueur getById(long id);
//Expert ExpertgetById(long id);
}
