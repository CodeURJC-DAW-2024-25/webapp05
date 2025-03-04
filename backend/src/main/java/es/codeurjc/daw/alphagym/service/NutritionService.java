package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import jakarta.annotation.PostConstruct;
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

    public Nutrition createNutrition(Nutrition nutrition) { // habra q añadirle un usuario
        Nutrition nutrition1 = new Nutrition(nutrition.getName(),nutrition.getCalories(), nutrition.getGoal(), nutrition.getDescription());
        if (nutrition.getImage() != null && !nutrition.getImage().equals("/images/emptyImage.png")) {
            nutrition1.setImage(nutrition.getImage());
        }
        nutritionRepository.save(nutrition1);
        return nutrition1;
    }

    public List<Nutrition> getAllNutritions(){
        List<Nutrition> listNutrition = nutritionRepository.findAll();
        return listNutrition.isEmpty() ? null : listNutrition;
    }


    
    public Nutrition getNutrition (Long id){
        Optional<Nutrition> theNutrition = nutritionRepository.findById(id);
        if (theNutrition.isPresent()){
            Nutrition nutrition = theNutrition.get();
            return nutrition;
        } else {
            return null;
        }
    }

    public Nutrition editDiet(Long id, Nutrition nutrition/* , User user*/){
        Optional<Nutrition> theDiet = nutritionRepository.findById(id);
        if(theDiet.isPresent()) {

            nutrition.setId(id);
            nutrition.setImage(theDiet.get().getImage());
            //training.setGymUser(user);
            nutritionRepository.save(nutrition);
            return nutrition;
        }
        return null;
    }

    public Nutrition deleteDiet(Long id){
        Optional<Nutrition> theDiet = nutritionRepository.findById(id);
        if (theDiet.isPresent()) {
            Nutrition nutrition = theDiet.get();
            //User user = routine.getGymUser();
            //user.getListRoutine().remove(routine);
            nutritionRepository.delete(nutrition);
            return nutrition;
        }
        return null;
    }

    
}

