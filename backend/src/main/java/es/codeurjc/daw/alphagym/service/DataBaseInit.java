package es.codeurjc.daw.alphagym.service;

import java.io.IOException;

import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import es.codeurjc.daw.alphagym.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DataBaseInit {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingRepository  trainingRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private TrainingService  trainingService;

    @Autowired
    private NutritionService nutritionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() throws IOException {

        //Users Examples
        User admin = new User("admin@admin.com", "admin", passwordEncoder.encode("adminpass"), "ADMIN", "USER");
        userRepository.save(admin);

        User user = new User("user", "user@user.com", passwordEncoder.encode("pass"), "USER");
        userRepository.save(user);

        //Trainings Examples
        Training chestPlan = new Training("Chest Plan","80%",60,"Increase volume", "Press de banca : 4x8-10\n" +
                "                Press inclinado con mancuernas: 4x10\n" +
                "                Fondos en paralelas: 3x10\n" +
                "                Cruces en polea: 4x12");

        Training armsPlan = new Training("Arms Plan","70%",45,"Increase volume", "Curl con barra: 4x10\n" +
                "                Curl martillo con mancuernas: 3x12\n" +
                "                Press francés: 4x10\n" +
                "                Fondos en paralelas: 3x10");

        Training legsPlan = new Training("Legs Plan","100%",90,"Increase volume", "Sentadillas: 4x8-10\n" +
                "               Peso muerto rumano: 3x12\n" +
                "               Extensiones de cuádriceps: 3x15\n" +
                "               Elevaciones de pantorrilla: 4x15");

        Training absPlan = new Training("Abs Plan", "50%", 30, "Lose weight", "Crunch en máquina: 4x15\n" +
                "                Toques de talón (oblicuos): 3x20\n" +
                "                Rodillo abdominal (ab wheel): 3x10\n" +
                "                Plancha (estática): 3x45s");

        Training backPlan = new Training ("Back Plan", "90%", 90, "Increase volume", "Dominadas: 4x8-10\n" +
                "                Remo con barra: 4x10\n" +
                "                Jalón al pecho en polea: 3x12\n" +
                "                Remo con mancuerna: 3x12" );

        Training shoulderPlan = new Training ("Shoulder Plan", "70%", 60, "Increase volume", "Press militar con barra: 4x10\n" +
                "                Elevaciones laterales: 3x12\n" +
                "                Pájaros (elevaciones posteriores): 3x12\n" +
                "                Encogimientos con barra (trapecios): 4x12");

        chestPlan.setImage("/images/plan_pecho.jpg");
        armsPlan.setImage("/images/plan_brazo.jpeg");
        legsPlan.setImage("/images/plan_pierna.jpeg");
        absPlan.setImage("/images/plan_abs.jpg");
        backPlan.setImage("/images/plan_espalda.jpeg");
        shoulderPlan.setImage("/images/plan_hombros.jpeg");

        trainingService.createTraining(chestPlan);
        trainingService.createTraining(armsPlan);
        trainingService.createTraining(legsPlan);
        trainingService.createTraining(absPlan);
        trainingService.createTraining(backPlan);
        trainingService.createTraining(shoulderPlan);

        //Nutritions Examples
        Nutrition caloricDeficit = new Nutrition ("Caloric Deficit", 100, "Lose weight",
                "Desayuno: 2 huevos + café\n" +
                        "Comida: Ensalada + 100g pechuga de pollo\n" +
                        "Merienda: Rebanada de pan integral\n" +
                        "Cena: Verduras + 150g de pescado");

        Nutrition caloricSurplus = new Nutrition ("Caloric Supurplus", 300, "Increase weight",
                "Desayuno: 6 huevos + 60g de avena con leche\n" +
                        "Comida: Taza de arroz + 150g de carne magra\n" +
                        "Merienda: Batido de proteinas + frutos secos\n" +
                        "Cena: 150g de salmon + 200g de patata");

        Nutrition maintenanceDiet = new Nutrition ("Maintenance Diet", 200, "Maintenance weight",
                "Desayuno: 2 huevos revueltos + 50 g de avena\n" +
                        "Comida: 150 g de pollo + ensalada con aceite\n" +
                        "Merienda: Yogur griego natural con almendras\n" +
                        "Cena: 120 g de salmón + verduras salteadas");

        caloricDeficit.setImage("/images/deficitcalorico.jpeg");
        caloricSurplus.setImage("/images/volumen.jpeg");
        maintenanceDiet.setImage("/images/mantenimiento.jpg");

        nutritionService.createNutrition(caloricDeficit);
        nutritionService.createNutrition(caloricSurplus);
        nutritionService.createNutrition(maintenanceDiet);
    }
    
}
