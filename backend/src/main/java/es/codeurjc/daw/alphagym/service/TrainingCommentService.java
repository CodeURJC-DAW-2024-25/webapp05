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
                " músculo y optimizar el entrenamiento.", "Descansos",6L);
        TrainingComment hidratación = new TrainingComment("Dado que esta rutina de ejercicios es muy aeróbica, la correcta" +
                " hidratación es extremadamente importante.", "Hidratación",1L);
        TrainingComment entidad1 = new TrainingComment("Comentario 1", "Entrenamiento Básico", 3L);
        TrainingComment entidad2 = new TrainingComment("Comentario 2", "Fuerza", 2L);
        TrainingComment entidad3 = new TrainingComment("Comentario 3", "Cardio Intenso", 5L);
        TrainingComment entidad4 = new TrainingComment("Comentario 4", "Rutina de Piernas", 6L);
        TrainingComment entidad5 = new TrainingComment("Comentario 5", "Espalda y Bíceps", 2L);
        TrainingComment entidad6 = new TrainingComment("Comentario 6", "Pecho y Tríceps", 6L);
        TrainingComment entidad7 = new TrainingComment("Comentario 7", "Entrenamiento Funcional", 5L);
        TrainingComment entidad8 = new TrainingComment("Comentario 8", "HIIT", 1L);
        TrainingComment entidad9 = new TrainingComment("Comentario 9", "Entrenamiento Full Body", 5L);
        TrainingComment entidad10 = new TrainingComment("Comentario 10", "CrossFit", 5L);
        TrainingComment entidad11 = new TrainingComment("Comentario 11", "Pilates", 5L);
        TrainingComment entidad12 = new TrainingComment("Comentario 12", "Yoga", 4L);
        TrainingComment entidad13 = new TrainingComment("Comentario 13", "Abdominales", 3L);
        TrainingComment entidad14 = new TrainingComment("Comentario 14", "Glúteos y Piernas", 6L);
        TrainingComment entidad15 = new TrainingComment("Comentario 15", "Rutina de Flexibilidad", 4L);

        createTrainingComment(descansos);
        createTrainingComment(hidratación);
        createTrainingComment(entidad1);
        createTrainingComment(entidad2);
        createTrainingComment(entidad3);
        createTrainingComment(entidad4);
        createTrainingComment(entidad5);
        createTrainingComment(entidad6);
        createTrainingComment(entidad7);
        createTrainingComment(entidad8);
        createTrainingComment(entidad9);
        createTrainingComment(entidad10);
        createTrainingComment(entidad11);
        createTrainingComment(entidad12);
        createTrainingComment(entidad13);
        createTrainingComment(entidad14);
        createTrainingComment(entidad15);

    }

        
    public List<TrainingComment> getAllTrainingComments() {
        List<TrainingComment> listTrainingComments = trainingCommentRepository.findAll();
        return listTrainingComments.isEmpty() ? null : listTrainingComments;
    }

    public List<TrainingComment> getTrainingComments(Long trainingId){
        List<TrainingComment> listTrainingComments = trainingCommentRepository.findByTrainingId(trainingId);
        return listTrainingComments.isEmpty() ? null : listTrainingComments;
    }

    public void createTrainingComment(TrainingComment trainingComment) {

        trainingCommentRepository.save(trainingComment);
    }
           
}

