package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.repository.UserRepository;
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
    private UserRepository userRepository;
    @Autowired
    private NutritionCommentService nutritionCommentService;

    public Nutrition createNutrition(Nutrition nutrition, User user) { 
        Nutrition newNutrition = new Nutrition(nutrition.getName(),nutrition.getCalories(), nutrition.getGoal(), nutrition.getDescription());
        newNutrition.setUser(user);
        if (nutrition.getImage() != null && !nutrition.getImage().equals("/images/emptyImage.png")) {
            newNutrition.setImage(nutrition.getImage());
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

    public Nutrition editDiet(Long id, Nutrition nutrition , User user){
        Optional<Nutrition> theDiet = nutritionRepository.findById(id);
        if(theDiet.isPresent()) {

            nutrition.setId(id);
            nutrition.setImage(theDiet.get().getImage());
            if (theDiet.get().getUser()!=null){
                nutrition.setUser(user);
            }
            nutritionRepository.save(nutrition);
            return nutrition;
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
                 userRepository.save(user); // Guardar los cambios en el usuario ya que no hay mapped by cascade
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


    /*public List<Nutrition> getUser(User user){
        Optional<List<Nutrition>> nutritions = nutritionRepository.findByUser(user);
        if(nutritions.isPresent()){
            return nutritions.get();
        }
        return null;
    }*/
    
}

