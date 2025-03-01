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


    @PostConstruct
    public void nutritionConstructor(){

        Nutrition caloricDeficit = new Nutrition ("Caloric Deficit", 100, "lose_weight", "Desayuno: 2 huevos + café\n" +
                "Comida: Ensalada + 100g pechuga de pollo\n" +
                "Merienda: Rebanada de pan integral\n" +
                "Cena: Verduras + 150g de pescado");

        Nutrition caloricSurplus = new Nutrition ("Caloric Supurplus", 300, "increase_weight", "Desayuno: 6 huevos + 60g de avena con leche\n" +
                "Comida: Taza de arroz + 150g de carne magra\n" +
                "Merienda: Batido de proteinas + frutos secos\n" +
                "Cena: 150g de salmon + 200g de patata");

        Nutrition maintenanceDiet = new Nutrition ("Maintenance Diet", 200, "maintenance_weight", "Desayuno: 2 huevos revueltos + 50 g de avena\n" +
                "Comida: 150 g de pollo + ensalada con aceite\n" +
                "Merienda: Yogur griego natural con almendras\n" +
                "Cena: 120 g de salmón + verduras salteadas");

        caloricDeficit.setImage("/images/deficitcalorico.jpeg");
        caloricSurplus.setImage("/images/volumen.jpeg");
        maintenanceDiet.setImage("/images/mantenimiento.jpg");

        createNutrition(caloricDeficit);
        createNutrition(caloricSurplus);
        createNutrition(maintenanceDiet);



    }
    public Nutrition createNutrition(Nutrition nutrition) { // habra q añadirle un usuario
        Nutrition nutrition1 = new Nutrition(nutrition.getName(),nutrition.getCalories(), nutrition.getGoal(), nutrition.getDescription(), nutrition.getImage());
        nutritionRepository.save(nutrition1);
        return nutrition1;
    }

    public List<Nutrition> getAllNutritions(){
        List<Nutrition> listNutrition = nutritionRepository.findAll();
        return listNutrition.isEmpty() ? null : listNutrition;
    }


    /*
    public Nutrition getNutrition (Long id){
        Optional<Nutrition> theNutrition = nutritionRepository.findById(id);
        if (theNutrition.isPresent()){
            Nutrition nutrition = theNutrition.get();
            return nutrition;
        } else {
            return null;
        }
    }

    public Nutrition updateNutrition(Long nutritionId, Nutrition nutrition, User user){
        Optional<Nutrition> theNutrition = nutritionRepository.findById(nutritionId);
        if (theNutrition.isPresent()){
            Nutrition nutrition1 = theNutrition.get();
            nutrition.setUser(user);
            nutrition.setId(nutritionId);
            nutritionRepository.save(nutrition1);
            
            return nutrition1;
        }
        return null;
    }*/

    
}

