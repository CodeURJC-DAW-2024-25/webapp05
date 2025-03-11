package es.codeurjc.daw.alphagym.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.alphagym.model.Nutrition;



public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
    List<Nutrition> findById(long id);
    List<Nutrition> findByName(String name);

}