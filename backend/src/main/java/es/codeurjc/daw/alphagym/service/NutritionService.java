package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.*;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
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
    private UserRepository userRepository;
    @Autowired
    private NutritionCommentService nutritionCommentService;

    public Nutrition createNutrition(Nutrition nutrition, User user) { 
        Nutrition newNutrition = new Nutrition(nutrition.getName(),nutrition.getCalories(), nutrition.getGoal(), nutrition.getDescription());
        newNutrition.setUser(user);
        if (nutrition.getImage() != null) {
            newNutrition.setImgNutrition(nutrition.getImgNutrition());
        }
        nutritionRepository.save(newNutrition);
        return newNutrition;
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

    public Nutrition editDiet(Long nutritionId, Nutrition nutrition , User user){
        Optional<Nutrition> existingNutritionOpt = nutritionRepository.findById(nutritionId);

        if (existingNutritionOpt.isPresent()) {
            Nutrition existingNutrition = existingNutritionOpt.get();

            existingNutrition.setName(nutrition.getName());
            existingNutrition.setCalories(nutrition.getCalories());
            existingNutrition.setGoal(nutrition.getGoal());
            existingNutrition.setDescription(nutrition.getDescription());

            if (nutrition.getImgNutrition() != null) {
                existingNutrition.setImgNutrition(nutrition.getImgNutrition());
                existingNutrition.setImage(true);
            }

            if (existingNutrition.getUser() != null) {
                existingNutrition.setUser(user);
            }

            return nutritionRepository.save(existingNutrition);
        }

        return null;
    }

    public Nutrition deleteDiet(Long id){
        Optional<Nutrition> theDiet = nutritionRepository.findById(id);
        if (theDiet.isPresent()) {
            Nutrition nutrition = theDiet.get();
            List<User> usersWithNutrition = userRepository.findByNutritionsContaining(nutrition);
             for (User user : usersWithNutrition) {
                 user.getNutritions().remove(nutrition);
                 userRepository.save(user); 
             }
            List<NutritionComment> nutritionComments = nutritionCommentRepository.findByNutritionId(id);

            for (NutritionComment nutritionComment : nutritionComments) {
                nutritionCommentRepository.delete(nutritionComment);

            }
            nutritionRepository.delete(nutrition);
            return nutrition;
        }
        return null;
    }

    public void subscribeNutrition(Long id , User user) {
         Optional<Nutrition> nutrition = nutritionRepository.findById(id);
         if (nutrition.isPresent()) {
             user.getNutritions().add(nutrition.get());
             userRepository.save(user);
         }
     }
 
     public void unsubscribeNutrition(Long id, User user) {
         Optional<Nutrition> nutrition = nutritionRepository.findById(id);
         if (nutrition.isPresent()) {
             user.getNutritions().remove(nutrition.get());
             userRepository.save(user);
         }
     }

     public Optional<Nutrition> findById(Long id) {
        return nutritionRepository.findById(id);
    }

    public List<Nutrition> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return nutritionRepository.findAll(); // If there is no search, return all
        }
        return nutritionRepository.findByName(name);
    }
    
}

