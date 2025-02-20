package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.NutritionComment;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

@Service
public class NutritionCommentService {

    @Autowired
    NutritionCommentRepository nutritionCommentRepository;



    public NutritionComment createNutritionComment(NutritionComment nutritionComment, User user) {
        NutritionComment nutritionComment1 = new NutritionComment(nutritionComment.getComment(),nutritionComment.getAuthor(),nutritionComment.getTitle());
        nutritionCommentRepository.save(nutritionComment1);
        return nutritionComment;
    }

}
