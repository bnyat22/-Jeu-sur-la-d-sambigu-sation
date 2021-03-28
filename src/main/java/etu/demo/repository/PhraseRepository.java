package etu.demo.repository;

import etu.demo.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {
 String FIND_PROJECTS = "select f.id as phrase_id ,f.phrase ,ma.id as mot_id,  ma.mot,ma.choix1 ,ma.choix2 , ma.choix3 , ma.choix4 from phras f inner join phrase_mot m on  m.phrase_id= f.id inner join mot_ambigu ma on ma.id = m.mot_id";
 String Find = "select f.id as phrase_id,f.phrase ,ma.id as mot_id, ma.mot ,ma.choix1 ,ma.choix2 , ma.choix3" +
         " , ma.choix4  from phras f inner join phrase_mot m on " +
         " m.phrase_id= f.id inner join mot_ambigu ma on ma.id = m.mot_id where  f.id =?1";
    Phrase getById(int id);

    @Query(value = FIND_PROJECTS , nativeQuery = true)
     List<Questions> findPhras();

@Query(value = Find , nativeQuery = true)
    List<Questions> findMot(int id);
}
