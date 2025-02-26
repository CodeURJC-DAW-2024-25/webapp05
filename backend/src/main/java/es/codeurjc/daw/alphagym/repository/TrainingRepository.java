package es.codeurjc.daw.alphagym.repository;

import java.util.List;
import java.util.Optional;

import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface TrainingRepository extends JpaRepository<Training, Long> {

    /*
    List<Training> findById(long id);
     */

}
