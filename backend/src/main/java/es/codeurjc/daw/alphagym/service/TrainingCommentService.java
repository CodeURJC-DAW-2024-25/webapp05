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
    private UserService userService;
    @Autowired
    private TrainingCommentRepository trainingCommentRepository;


    @PostConstruct
    public void trainingCommentConstructor() {
        TrainingComment descansos = new TrainingComment("Es importante hacer descansos entre series para evitar fatigar el" +
                " músculo y optimizar el entrenamiento.", "Descansos");

        TrainingComment hidratación = new TrainingComment("Dado que esta rutina de ejercicios es muy aeróbica, la correcta" +
                " hidratación es extremadamente importante.", "Hidratación");


        createTrainingComment(descansos);
        createTrainingComment(hidratación);

    }

        
    public List<TrainingComment> getAllTrainingComments(Long trainingId) {
        List<TrainingComment> listTrainingComments = trainingCommentRepository.findAll();
        return listTrainingComments.isEmpty() ? null : listTrainingComments;
    }

    public void createTrainingComment(TrainingComment trainingComment) {

        trainingCommentRepository.save(trainingComment);
    }
           
    /*public Training getTraining(Long id){
        Optional<Training> theTraining = trainingRepository.findById(id);
        if (theTraining.isPresent()) {
            return theTraining.get();
        }
        return null;
    }   */
}

