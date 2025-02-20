package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.Nutrition;
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
public class NutritionService {

    @Autowired
    private NutritionRepository nutritionRepository;
    @Autowired
    private NutritionCommentRepository nutritionCommentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NutritionCommentService nutritionCommentService;

    public Nutrition createNutrition(Nutrition nutrition, User user) {
        Nutrition nutrition1 = new Nutrition(nutrition.getName(),nutrition.getTitle(),nutrition.getDescription(), nutrition.getImage());
        nutritionRepository.save(nutrition1);
        return nutrition1;
    }
}

