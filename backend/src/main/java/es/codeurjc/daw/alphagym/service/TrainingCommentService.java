package es.codeurjc.daw.alphagym.service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import es.codeurjc.daw.alphagym.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.alphagym.dto.TrainingCommentDTO;
import es.codeurjc.daw.alphagym.dto.TrainingCommentMapper;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class TrainingCommentService {

    @Autowired
    private TrainingCommentRepository trainingCommentRepository;

    @Autowired
    private TrainingCommentMapper trainingCommentMapper;
        
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

    @Transactional(readOnly = true)
    public boolean isOwnerComment(Long commentId, Authentication authentication) {
        return trainingCommentRepository.findById(commentId)
                .map(comment -> {
                    User user = comment.getUser();
                    return user != null && authentication.getName().equals(user.getEmail());
                })
                .orElse(false);
    }

    public String editCommentAdminService(Model model, @PathVariable Long commentId) {
        TrainingComment comment = trainingCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            Training training = comment.getTraining();
            return "redirect:/trainingComments/" + training.getId() + "/" + commentId + "/editcomment";
        } else {
            return "redirect:/admin";
        }
    }

    public String deleteCommentAdminService(Model model, @PathVariable Long commentId) {
        TrainingComment comment = trainingCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            Training training = comment.getTraining();
            deleteCommentbyId(training, commentId);
        }
        return "redirect:/admin";
    }

    //REST METHODS

    public Collection<TrainingCommentDTO> getAllTrainingCommentsDTO() {
        return trainingCommentMapper.toDTOs(trainingCommentRepository.findAll());
    }

    public Collection<TrainingCommentDTO> getTrainingCommentsByIdDTO(Long trainingId) {
        return trainingCommentMapper.toDTOs(trainingCommentRepository.findByTrainingId(trainingId));
    }
    
    public List<TrainingCommentDTO> getPaginatedCommentsDTO(Long trainingId, int page, int limit) {
        return trainingCommentRepository
            .findByTrainingId(trainingId, PageRequest.of(page, limit))
            .map(trainingCommentMapper::toDTO)
            .toList();
    }

    public TrainingCommentDTO createTrainingCommentDTO(TrainingCommentDTO trainingCommentDTO) {
        TrainingComment trainingComment = toDomain(trainingCommentDTO);
        trainingComment = trainingCommentRepository.save(trainingComment);
        return toDTO(trainingComment);
    }

    public TrainingCommentDTO deleteCommentbyIdDTO(Long commentId) {
        Optional<TrainingComment> trainingComment = trainingCommentRepository.findById(commentId);
        trainingCommentRepository.deleteById(commentId);
        return toDTO(trainingComment.orElse(null));
    }

    public TrainingCommentDTO replaceTrainingCommentDTO(Long trainingId, TrainingCommentDTO updatedCommentDTO) {

        if (trainingCommentRepository.existsById(trainingId)) {
            TrainingComment updatedComment = toDomain(updatedCommentDTO);
            updatedComment.setId(trainingId);
            trainingCommentRepository.save(updatedComment);
            return toDTO(updatedComment);
        } else {
            throw new NoSuchElementException("No se encontró el comentario con id: " + trainingId);
        }
    }



    //Send to API
    public TrainingCommentDTO toDTO(TrainingComment trainingComment) {
        return trainingCommentMapper.toDTO(trainingComment);
    }
    //Return a comment List to API
    public Collection<TrainingCommentDTO> toDTOs(Collection<TrainingComment> trainingComments) {
        return trainingCommentMapper.toDTOs(trainingComments);
    }
    //Data which comes from API result converted to the expected structure in the backend
    public TrainingComment toDomain(TrainingCommentDTO trainingCommentDTO) {
        return trainingCommentMapper.toDomain(trainingCommentDTO);
    }

}

