package etu.demo.repository;

import etu.demo.domain.Intermédiaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface IntermidaireRepository extends JpaRepository<Intermédiaire , Long> {
    Intermédiaire getById(long id);

    @Modifying
    @Transactional
    @Query(value = "insert into intermidaire values(0,0,?1)" , nativeQuery = true)
    void insert(long id);

    int countById(long id);
}
