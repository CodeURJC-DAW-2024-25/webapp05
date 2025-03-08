package es.codeurjc.daw.alphagym.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.NutritionComment;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import es.codeurjc.daw.alphagym.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DataBaseInit {

    @Autowired  
        private UserService userService;    

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

    @Autowired
    private TrainingCommentService trainingCommentService;
    @Autowired
    private NutritionCommentService nutritionCommentService;

    @PostConstruct
    public void init() throws IOException {

        //Users Examples
        User admin = new User("admin", "admin@admin.com", passwordEncoder.encode("adminpass"), "ADMIN", "USER");
        //userRepository.save(admin);
        userService.save(admin);

        User user = new User("user", "user@user.com", passwordEncoder.encode("pass"), "USER");
        //userRepository.save(user);
        userService.save(user);

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

        chestPlan = trainingService.createTraining(chestPlan, null);
        armsPlan = trainingService.createTraining(armsPlan, null);
        legsPlan = trainingService.createTraining(legsPlan, null);
        absPlan = trainingService.createTraining(absPlan, null);
        backPlan = trainingService.createTraining(backPlan, null);
        shoulderPlan = trainingService.createTraining(shoulderPlan, null);

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

        caloricDeficit = nutritionService.createNutrition(caloricDeficit);
        caloricSurplus = nutritionService.createNutrition(caloricSurplus);
        maintenanceDiet = nutritionService.createNutrition(maintenanceDiet);

        //training comments examples
        TrainingComment descansos = new TrainingComment("Es importante hacer descansos entre series para evitar fatigar el músculo y optimizar el entrenamiento.", "Descansos");
        TrainingComment hidratación = new TrainingComment("Dado que esta rutina de ejercicios es muy aeróbica, la correcta hidratación es extremadamente importante.", "Hidratación");
        TrainingComment entidad1 = new TrainingComment("Comentario 1", "Entrenamiento Básico");
        TrainingComment entidad2 = new TrainingComment("Comentario 2", "Fuerza");
        TrainingComment entidad3 = new TrainingComment("Comentario 3", "Cardio Intenso");
        TrainingComment entidad4 = new TrainingComment("Comentario 4", "Rutina de Piernas");
        TrainingComment entidad5 = new TrainingComment("Comentario 5", "Espalda y Bíceps");
        TrainingComment entidad6 = new TrainingComment("Comentario 6", "Pecho y Tríceps");
        TrainingComment entidad7 = new TrainingComment("Comentario 7", "Entrenamiento Funcional");
        TrainingComment entidad8 = new TrainingComment("Comentario 8", "HIIT");
        TrainingComment entidad9 = new TrainingComment("Comentario 9", "Entrenamiento Full Body");
        TrainingComment entidad10 = new TrainingComment("Comentario 10", "CrossFit");
        TrainingComment entidad11 = new TrainingComment("Comentario 11", "Pilates");
        TrainingComment entidad12 = new TrainingComment("Comentario 12", "Yoga");
        TrainingComment entidad13 = new TrainingComment("Comentario 13", "Abdominales");
        TrainingComment entidad14 = new TrainingComment("Comentario 14", "Glúteos y Piernas");
        TrainingComment entidad15 = new TrainingComment("Comentario 15", "Rutina de Flexibilidad");

        trainingCommentService.createTrainingComment(descansos, chestPlan);
        trainingCommentService.createTrainingComment(hidratación, chestPlan);
        trainingCommentService.createTrainingComment(entidad1, chestPlan);
        trainingCommentService.createTrainingComment(entidad2, armsPlan);
        trainingCommentService.createTrainingComment(entidad3, chestPlan);
        trainingCommentService.createTrainingComment(entidad4, chestPlan);
        trainingCommentService.createTrainingComment(entidad5, legsPlan);
        trainingCommentService.createTrainingComment(entidad6, chestPlan);
        trainingCommentService.createTrainingComment(entidad7, absPlan);
        trainingCommentService.createTrainingComment(entidad8, chestPlan);
        trainingCommentService.createTrainingComment(entidad9, chestPlan);
        trainingCommentService.createTrainingComment(entidad10, backPlan);
        trainingCommentService.createTrainingComment(entidad11, chestPlan);
        trainingCommentService.createTrainingComment(entidad12, chestPlan);
        trainingCommentService.createTrainingComment(entidad13, shoulderPlan);
        trainingCommentService.createTrainingComment(entidad14, chestPlan);
        trainingCommentService.createTrainingComment(entidad15, chestPlan);

        //nutrition comment examples
        NutritionComment variedad = new NutritionComment("Es importante comer variado", "Variedad");
        NutritionComment frutas = new NutritionComment("Es necesario comer mucha fruta", "Frutas");
        NutritionComment comentario1 = new NutritionComment("Comentario 1", "Entrenamiento Básico");
        NutritionComment comentario2 = new NutritionComment("Comentario 2", "Fuerza");
        NutritionComment comentario3 = new NutritionComment("Comentario 3", "Cardio Intenso");
        NutritionComment comentario4 = new NutritionComment("Comentario 4", "Rutina de Piernas");
        NutritionComment comentario5 = new NutritionComment("Comentario 5", "Espalda y Bíceps");
        NutritionComment comentario6 = new NutritionComment("Comentario 6", "Pecho y Tríceps");
        NutritionComment comentario7 = new NutritionComment("Comentario 7", "Entrenamiento Funcional");
        NutritionComment comentario8 = new NutritionComment("Comentario 8", "HIIT");
        NutritionComment comentario9 = new NutritionComment("Comentario 9", "Entrenamiento Full Body");
        NutritionComment comentario10 = new NutritionComment("Comentario 10", "CrossFit");
        NutritionComment comentario11 = new NutritionComment("Comentario 11", "Pilates");
        NutritionComment comentario12 = new NutritionComment("Comentario 12", "Yoga");
        NutritionComment comentario13 = new NutritionComment("Comentario 13", "Abdominales");
        NutritionComment comentario14 = new NutritionComment("Comentario 14", "Glúteos y Piernas");
        NutritionComment comentario15 = new NutritionComment("Comentario 15", "Rutina de Flexibilidad");


        nutritionCommentService.createNutritionComment(variedad,caloricDeficit);
        nutritionCommentService.createNutritionComment(frutas,caloricDeficit);
        nutritionCommentService.createNutritionComment(comentario1,caloricDeficit);
        nutritionCommentService.createNutritionComment(comentario2,caloricSurplus);
        nutritionCommentService.createNutritionComment(comentario3,caloricDeficit);
        nutritionCommentService.createNutritionComment(comentario4,caloricDeficit);
        nutritionCommentService.createNutritionComment(comentario5,caloricSurplus);
        nutritionCommentService.createNutritionComment(comentario6,caloricDeficit);
        nutritionCommentService.createNutritionComment(comentario7,caloricSurplus);
        nutritionCommentService.createNutritionComment(comentario8,caloricDeficit);
        nutritionCommentService.createNutritionComment(comentario9,caloricDeficit);
        nutritionCommentService.createNutritionComment(comentario10,maintenanceDiet);
        nutritionCommentService.createNutritionComment(comentario11,caloricDeficit);
        nutritionCommentService.createNutritionComment(comentario12,caloricDeficit);
        nutritionCommentService.createNutritionComment(comentario13,maintenanceDiet);
        nutritionCommentService.createNutritionComment(comentario14,caloricDeficit);
        nutritionCommentService.createNutritionComment(comentario15,caloricDeficit);


    }
    
}
