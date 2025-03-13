package es.codeurjc.daw.alphagym.service;

import java.util.List;
import es.codeurjc.daw.alphagym.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;

@Service
public class TrainingCommentService {

    @Autowired
    private TrainingCommentRepository trainingCommentRepository;
        
    public List<TrainingComment> getAllTrainingComments() {
        List<TrainingComment> listTrainingComments = trainingCommentRepository.findAll();
        return listTrainingComments.isEmpty() ? null : listTrainingComments;
    }

    public List<TrainingComment> getTrainingComments(Long trainingId){
        List<TrainingComment> listTrainingComments = trainingCommentRepository.findByTrainingId(trainingId);
        return listTrainingComments.isEmpty() ? null : listTrainingComments;
    }

    public void createTrainingComment(TrainingComment trainingComment,Training training, User user) {
        trainingComment.setUser(user);
        trainingComment.setTraining(training);
        trainingCommentRepository.save(trainingComment);
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

    public void unreportCommentbyId(Long commentId) {
        TrainingComment comment = trainingCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setIsNotified(false);
            trainingCommentRepository.save(comment);
        }
    }

    public TrainingComment getCommentById(Long commentId) {
         return trainingCommentRepository.findById(commentId).orElse(null);
     }
 
    public void updateComment(TrainingComment comment) {
         trainingCommentRepository.save(comment);
     }

    public Long[] getReportAmmmounts() {
        Long reported = trainingCommentRepository.countByIsNotified(true);
        Long notReported = trainingCommentRepository.countByIsNotified(false);
        return new Long[] {reported, notReported};
    }      

    public List<TrainingComment> getReportedComments() {
        List<TrainingComment> listTrainingComments = trainingCommentRepository.findByIsNotified(true);
        return listTrainingComments.isEmpty() ? null : listTrainingComments;
    }     
    
    public List<TrainingComment> getPaginatedComments(Long trainingId, int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<TrainingComment> commentsPage = trainingCommentRepository.findByTrainingId(trainingId, pageable);
        return commentsPage.getContent();
    }
    
}

