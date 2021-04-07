package etu.demo.repository;

import etu.demo.domain.MotAmbigu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotAmbiguRepository extends JpaRepository<MotAmbigu , Long> {

    MotAmbigu getById(int id);

}
