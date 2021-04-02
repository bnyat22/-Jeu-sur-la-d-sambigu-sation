package etu.demo.repository;

import etu.demo.domain.Expert;
import etu.demo.domain.Joueur;
import etu.demo.domain.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoueurRepository extends JpaRepository<Joueur , Long> {
Joueur getById(long id);
//Expert ExpertgetById(long id);

@Query(value = "SELECT \n" +
        " (SELECT COUNT(*)+1 FROM joueur  WHERE point>j.point) AS rank_upper ,\n" +
        "    j.user_id, \n" +
        "     u.pseudo,\n" +
        "    j.point\n" +
        "FROM \n" +
        "    joueur j , utilisateur u where j.user_id = u.id order by rank_upper" , nativeQuery = true)
    List<Rank> getRank();
}
