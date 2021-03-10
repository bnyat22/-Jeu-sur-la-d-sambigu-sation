package etu.demo.repository;

import etu.demo.domain.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpertRepository extends JpaRepository<Expert , Long> {
    Expert getById(long id);
}
