package etu.demo.repository;

import etu.demo.domain.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {

    Phrase getById(int id);

}
