package es.codeurjc.daw.alphagym.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.alphagym.model.NutritionComment;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.model.Nutrition;

@Service
public class NutritionCommentService {

    @Autowired
    private NutritionCommentRepository nutritionCommentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NutritionRepository nutritionRepository;

    public List<NutritionComment> getAllNutritionComments() {
        List<NutritionComment> listNutritionComment = nutritionCommentRepository.findAll();
        return listNutritionComment.isEmpty() ? null : listNutritionComment;
    }

    public List<NutritionComment> getNutritionComments(Long nutritionId) {
        List<NutritionComment> listNutritionComments = nutritionCommentRepository.findByNutritionId(nutritionId);
        return listNutritionComments.isEmpty() ? null : listNutritionComments;
    }

    public void createNutritionComment(NutritionComment nutritionComment, Nutrition nutrition) {
        nutritionComment.setNutrition(nutrition);
        nutritionComment = nutritionCommentRepository.save(nutritionComment);
        nutrition.getComments().add(nutritionComment);
    }

    public void deleteCommentbyId(Nutrition nutrition, Long commentId) {
        NutritionComment comment = nutritionCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            nutrition.getComments().remove(comment);
        }
        nutritionCommentRepository.deleteById(commentId);
    }

    public void reportCommentbyId(Long commentId) {
        NutritionComment comment = nutritionCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setIsNotified(true);
            nutritionCommentRepository.save(comment);
        }
    }

    public NutritionComment getCommentById(Long commentId) {
        return nutritionCommentRepository.findById(commentId).orElse(null);
    }

    public void updateComment(NutritionComment comment) {
        nutritionCommentRepository.save(comment); // Save updates to the database
    }

    public Long[] getReportAmmmounts() {
        Long reported = nutritionCommentRepository.countByIsNotified(true);
        Long notReported = nutritionCommentRepository.countByIsNotified(false);
        return new Long[] {reported, notReported};
    } 
}
