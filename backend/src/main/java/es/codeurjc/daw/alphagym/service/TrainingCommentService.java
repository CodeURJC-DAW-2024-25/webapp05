package es.codeurjc.daw.alphagym.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.service.TrainingCommentService;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import es.codeurjc.daw.alphagym.service.UserService;
import jakarta.annotation.PostConstruct;


@Service
public class TrainingCommentService {

    @Autowired
    public UserService userService;
    @Autowired
    public TrainingCommentRepository trainingCommentRepository;
    /*@Autowired
    public TrainingRepository trainingRepository;
    @Autowired
    public TrainingService trainingService;*/

    @PostConstruct
    public void trainingCommentConstructor() {
        TrainingComment descansos = new TrainingComment("Es importante hacer descansos entre series para evitar fatigar el\r\n" + //
                "              músculo y optimizar el entrenamiento.", "Descansos");

        TrainingComment hidratación = new TrainingComment("Dado que esta rutina de ejercicios es muy aeróbica, la correcta\r\n" + //
                "              hidratación es extremadamente importante.", "Hidratación");


        //Optional<Training> chestPlan = trainingRepository.findById((long) 1);
        //Optional<Training> armsPlan = trainingRepository.findById((long) 2);
        //descansos.setTraining(chestPlan.get());//no sé pq no va, vuelvo en nada
        //hidratación.setTraining(armsPlan.get());

        createTrainingComment(descansos);
        createTrainingComment(hidratación);

    }

        
    /*public List<TrainingComment> getAllTrainingComments(Long trainingId) {
        List<TrainingComment> listTrainingComments = trainingCommentRepository.findByTrainingId(trainingId);
        return listTrainingComments.isEmpty() ? null : listTrainingComments;
    }*/

    public TrainingComment createTrainingComment(TrainingComment trainingComment) {
        trainingCommentRepository.save(trainingComment);
        return trainingComment;
    }
           
    /*public Training getTraining(Long id){
        Optional<Training> theTraining = trainingRepository.findById(id);
        if (theTraining.isPresent()) {
            return theTraining.get();
        }
        return null;
    }   */
}

