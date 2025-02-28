package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class TrainingService {


    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TrainingCommentRepository trainingCommentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TrainingCommentService trainingCommentService;


    @PostConstruct
    public void trainingConstructor(){
        Training chestPlan = new Training("Chest Plan","80%",60,"increase_volume", "Press de banca : 4x8-10\n" +
                "                Press inclinado con mancuernas: 4x10\n" +
                "                Fondos en paralelas: 3x10\n" +
                "                Cruces en polea: 4x12");

        Training armsPlan = new Training("Arms Plan","70%",45,"increase_volume", "Curl con barra: 4x10\n" +
                "                Curl martillo con mancuernas: 3x12\n" +
                "                Press francés: 4x10\n" +
                "                Fondos en paralelas: 3x10");

        Training legsPlan = new Training("Legs Plan","100%",90,"increase_volume", "Sentadillas: 4x8-10\n" +
                "               Peso muerto rumano: 3x12\n" +
                "               Extensiones de cuádriceps: 3x15\n" +
                "               Elevaciones de pantorrilla: 4x15");

        Training absPlan = new Training("Abs Plan", "50%", 30, "lose_weight", "Crunch en máquina: 4x15\n" +
                "                Toques de talón (oblicuos): 3x20\n" +
                "                Rodillo abdominal (ab wheel): 3x10\n" +
                "                Plancha (estática): 3x45s");

        Training backPlan = new Training ("Back Plan", "90%", 90, "increase_volume", "Dominadas: 4x8-10\n" +
                "                Remo con barra: 4x10\n" +
                "                Jalón al pecho en polea: 3x12\n" +
                "                Remo con mancuerna: 3x12" );

        Training shoulderPlan = new Training ("Shoulder Plan", "70%", 60, "increase_volume", "Press militar con barra: 4x10\n" +
                "                Elevaciones laterales: 3x12\n" +
                "                Pájaros (elevaciones posteriores): 3x12\n" +
                "                Encogimientos con barra (trapecios): 4x12");

        chestPlan.setImage("/images/plan_pecho.jpg");
        armsPlan.setImage("/images/plan_brazo.jpeg");
        legsPlan.setImage("/images/plan_pierna.jpeg");
        absPlan.setImage("/images/plan_abs.jpg");
        backPlan.setImage("/images/plan_espalda.jpeg");
        shoulderPlan.setImage("/images/plan_hombros.jpeg");

        createTraining(chestPlan);
        createTraining(armsPlan);
        createTraining(legsPlan);
        createTraining(absPlan);
        createTraining(backPlan);
        createTraining(shoulderPlan);

    }

    public void createTraining(Training training) {

        trainingRepository.save(training);
    }

    public List<Training> getAllTrainings(){
        List<Training> listTraining = trainingRepository.findAll();
        return listTraining.isEmpty() ? null : listTraining;
    }
/*
    public Collection<Training> getUserTrainings(Long id){
        Optional<List<Training>> listTrainingUser = trainingRepository.findByUser(userService.getUser(id));
        if(listTrainingUser.isPresent()){
            return listTrainingUser.get();
        }
        return null;
    }
    */

}
