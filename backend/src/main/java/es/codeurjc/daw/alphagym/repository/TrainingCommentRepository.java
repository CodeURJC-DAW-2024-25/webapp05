package es.codeurjc.daw.alphagym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.alphagym.model.TrainingComment;



public interface TrainingCommentRepository extends JpaRepository<TrainingComment, Long> {
    List<TrainingComment> findById(long id);
    
}
