package etu.demo.repository;

import etu.demo.domain.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {

    Phrase getById(int id);


}
