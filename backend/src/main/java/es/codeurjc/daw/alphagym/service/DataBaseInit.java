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

        chestPlan.setImageDefault("/images/plan_pecho.jpg");
        armsPlan.setImageDefault("/images/plan_brazo.jpeg");
        legsPlan.setImageDefault("/images/plan_pierna.jpeg");
        absPlan.setImageDefault("/images/plan_abs.jpg");
        backPlan.setImageDefault("/images/plan_espalda.jpeg");
        shoulderPlan.setImageDefault("/images/plan_hombros.jpeg");

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

        caloricDeficit = nutritionService.createNutrition(caloricDeficit, null);
        caloricSurplus = nutritionService.createNutrition(caloricSurplus, null);
        maintenanceDiet = nutritionService.createNutrition(maintenanceDiet, null);

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

        entidad3.setIsNotified(true);
        entidad5.setIsNotified(true);
        entidad11.setIsNotified(true);

        trainingCommentService.createTrainingComment(descansos, chestPlan,null);
        trainingCommentService.createTrainingComment(hidratación, chestPlan, null);
        trainingCommentService.createTrainingComment(entidad1, chestPlan,null);
        trainingCommentService.createTrainingComment(entidad2, armsPlan,null);
        trainingCommentService.createTrainingComment(entidad3, chestPlan,null);
        trainingCommentService.createTrainingComment(entidad4, chestPlan,null);
        trainingCommentService.createTrainingComment(entidad5, legsPlan,null);
        trainingCommentService.createTrainingComment(entidad6, chestPlan,null);
        trainingCommentService.createTrainingComment(entidad7, absPlan,null);
        trainingCommentService.createTrainingComment(entidad8, chestPlan,null);
        trainingCommentService.createTrainingComment(entidad9, chestPlan,null);
        trainingCommentService.createTrainingComment(entidad10, backPlan,null);
        trainingCommentService.createTrainingComment(entidad11, chestPlan,null);
        trainingCommentService.createTrainingComment(entidad12, chestPlan,null);
        trainingCommentService.createTrainingComment(entidad13, shoulderPlan,null);
        trainingCommentService.createTrainingComment(entidad14, chestPlan,null);
        trainingCommentService.createTrainingComment(entidad15, chestPlan,null);

        //nutrition comment examples
        NutritionComment variedad = new NutritionComment("Es importante comer variado", "Variedad");
        NutritionComment frutas = new NutritionComment("Es necesario comer mucha fruta", "Frutas");
        NutritionComment comentario1 = new NutritionComment("Proteínas esenciales", "Importante consumir suficiente proteína para la recuperación muscular.");
        NutritionComment comentario2 = new NutritionComment("Hidratación", "Beber suficiente agua mejora el rendimiento y la recuperación.");
        NutritionComment comentario3 = new NutritionComment("Carbohidratos y energía", "Los carbohidratos son clave para mantener la energía durante el ejercicio.");
        NutritionComment comentario4 = new NutritionComment("Grasas saludables", "Las grasas saludables son esenciales para la función hormonal y la energía.");
        NutritionComment comentario5 = new NutritionComment("Vitaminas y minerales", "Consumir frutas y verduras aporta nutrientes esenciales para el cuerpo.");
        NutritionComment comentario6 = new NutritionComment("Timing de comidas", "Comer antes y después del entrenamiento mejora el rendimiento y la recuperación.");
        NutritionComment comentario7 = new NutritionComment("Suplementación", "Los suplementos pueden ayudar, pero la dieta siempre debe ser la base.");
        NutritionComment comentario8 = new NutritionComment("Fibra y digestión", "Consumir suficiente fibra mejora la digestión y la salud intestinal.");
        NutritionComment comentario9 = new NutritionComment("Déficit calórico", "Para perder peso, es clave consumir menos calorías de las que se gastan.");
        NutritionComment comentario10 = new NutritionComment("Superávit calórico", "Para ganar masa muscular, es necesario un superávit calórico.");
        NutritionComment comentario11 = new NutritionComment("Desayuno equilibrado", "Un buen desayuno debe incluir proteínas, carbohidratos y grasas saludables.");
        NutritionComment comentario12 = new NutritionComment("Ayuno intermitente", "El ayuno intermitente puede ayudar a controlar el apetito y mejorar la quema de grasa.");
        NutritionComment comentario13 = new NutritionComment("Electrolitos", "Los electrolitos son clave para la hidratación, especialmente después de entrenar.");
        NutritionComment comentario14 = new NutritionComment("Reducción de azúcar", "Disminuir el azúcar mejora la salud metabólica y reduce la inflamación.");
        NutritionComment comentario15 = new NutritionComment("Comidas balanceadas", "Cada comida debe incluir proteínas, grasas saludables y carbohidratos adecuados.");
        
        comentario5.setIsNotified(true);
        comentario7.setIsNotified(true);

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
