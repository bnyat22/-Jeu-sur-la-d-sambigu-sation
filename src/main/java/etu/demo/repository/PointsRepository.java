package etu.demo.repository;

import etu.demo.domain.MotAmbigu;
import etu.demo.domain.Points;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PointsRepository extends JpaRepository<Points,Long > {

    List<Points> getPointsByPhrase_id(int id);
    //Points getByMotAmbigu_id(MotAmbigu motAmbigu);

}
