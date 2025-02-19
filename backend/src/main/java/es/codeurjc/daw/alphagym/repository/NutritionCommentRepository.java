package es.codeurjc.daw.alphagym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.alphagym.model.NutritionComment;

public interface NutritionCommentRepository extends JpaRepository<NutritionComment, Long> {
    List<NutritionComment> findById(long id);
    
}
