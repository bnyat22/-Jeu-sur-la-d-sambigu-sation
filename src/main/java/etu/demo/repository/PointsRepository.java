package etu.demo.repository;

import etu.demo.domain.Points;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PointsRepository extends JpaRepository<Points,Long > {
Points getPointsByPhrase_id(int id);
}
