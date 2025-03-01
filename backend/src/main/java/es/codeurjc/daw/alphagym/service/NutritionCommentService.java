package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.NutritionComment;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

@Service
public class NutritionCommentService {
    
    @Autowired
    private NutritionCommentRepository nutritionCommentRepository;
    @Autowired
    private UserService userService;


    @PostConstruct
    public void nutritionCommentConstructor() {
        NutritionComment variedad = new NutritionComment("Es importante comer variado", "Variedad");

        NutritionComment hidratación = new NutritionComment("Es necesario beber mucha agua", "Hidratación");


        createNutritionComment(variedad);
        createNutritionComment(hidratación);

    }


    public List<NutritionComment> getAllNutritionComments() {
        List<NutritionComment> listNutritionComment = nutritionCommentRepository.findAll();
        return listNutritionComment.isEmpty() ? null : listNutritionComment;
    }

    public void createNutritionComment(NutritionComment nutritionComment) {

        nutritionCommentRepository.save(nutritionComment);
    }

    /*
    public NutritionComment createNutritionComment(NutritionComment nutritionComment, User user) {
        NutritionComment nutritionComment1 = new NutritionComment(nutritionComment.getComment(),nutritionComment.getAuthor(),nutritionComment.getTitle());
        nutritionCommentRepository.save(nutritionComment1);
        return nutritionComment;
    }
*/
}
