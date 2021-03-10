package etu.demo.repository;

import etu.demo.domain.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {
 String FIND_PROJECTS = "SELECT phrase, niveau , mot ,choix1 , choix2 , choix3 , choix4 FROM phras,mot_ambigu where phras.id = mot_ambigu.id";
    Phrase getById(int id);

    @Query(value = FIND_PROJECTS, nativeQuery = true)
     List<Object[]> findPhras();

}
