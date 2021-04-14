package etu.etu.repository;

import etu.etu.domain.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ExpertRepository extends JpaRepository<Expert , Long> {
    Expert getById(long id);

    @Modifying
    @Transactional
    @Query(value = "insert into expert values(0,?1)" , nativeQuery = true)
    void insert(long id);


    int countById(long id);

}
