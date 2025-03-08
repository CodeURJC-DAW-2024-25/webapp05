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
    @Autowired
    private TrainingRepository traingRepository;

        
    public List<TrainingComment> getAllTrainingComments() {
        List<TrainingComment> listTrainingComments = trainingCommentRepository.findAll();
        return listTrainingComments.isEmpty() ? null : listTrainingComments;
    }

    public List<TrainingComment> getTrainingComments(Long trainingId){
        List<TrainingComment> listTrainingComments = trainingCommentRepository.findByTrainingId(trainingId);
        return listTrainingComments.isEmpty() ? null : listTrainingComments;
    }

    public void createTrainingComment(TrainingComment trainingComment,Training training) {
        trainingComment.setTraining(training);
        trainingComment = trainingCommentRepository.save(trainingComment);
        training.getComments().add(trainingComment);
        
    }

    public void deleteCommentbyId(Training training, Long commentId) {
        TrainingComment comment = trainingCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            training.getComments().remove(comment);            
        }
        trainingCommentRepository.deleteById(commentId);
        
    }

    public void reportCommentbyId(Long commentId) {
        TrainingComment comment = trainingCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setIsNotified(true);
            trainingCommentRepository.save(comment);
        }
    }

           
}

