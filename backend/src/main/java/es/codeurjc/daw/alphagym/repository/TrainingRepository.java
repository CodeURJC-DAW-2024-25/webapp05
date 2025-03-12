package es.codeurjc.daw.alphagym.repository;

import java.util.List;
import es.codeurjc.daw.alphagym.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByName(String name);
    List<Training> findById(long id);
    
}
