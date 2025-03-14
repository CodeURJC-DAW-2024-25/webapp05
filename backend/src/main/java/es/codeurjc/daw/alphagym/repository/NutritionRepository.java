package es.codeurjc.daw.alphagym.repository;
import java.util.List;
import java.util.Optional;

import es.codeurjc.daw.alphagym.model.Training;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.daw.alphagym.model.Nutrition;

public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
    List<Nutrition> findById(long id);
    List<Nutrition> findByName(String name);

    @EntityGraph(attributePaths = "user")
    Optional<Nutrition> findWithUserById(Long id);
}