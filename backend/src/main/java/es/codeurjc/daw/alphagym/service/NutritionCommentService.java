package es.codeurjc.daw.alphagym.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.NutritionComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;

@Service
public class NutritionCommentService {

    @Autowired
    private NutritionCommentRepository nutritionCommentRepository;

    private NutritionCommentService nutritionCommentService;

    public List<NutritionComment> getAllNutritionComments() {
        List<NutritionComment> listNutritionComment = nutritionCommentRepository.findAll();
        return listNutritionComment.isEmpty() ? null : listNutritionComment;
    }

    public List<NutritionComment> getNutritionComments(Long nutritionId) {
        List<NutritionComment> listNutritionComments = getPaginatedComments(nutritionId,0,10);
        return listNutritionComments.isEmpty() ? null : listNutritionComments;
    }

    public void createNutritionComment(NutritionComment nutritionComment, Nutrition nutrition, User user) {
        nutritionComment.setUser(user);
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

    public void unreportCommentbyId(Long commentId) {
        NutritionComment comment = nutritionCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setIsNotified(false);
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

    public List<NutritionComment> getPaginatedComments(Long nutritionId, int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<NutritionComment> commentsPage = nutritionCommentRepository.findByNutritionId(nutritionId, pageable);
        return commentsPage.getContent();
    }

    public List<NutritionComment> getReportedComments() {
        List<NutritionComment> listNutritionComments = nutritionCommentRepository.findByIsNotified(true);
        return listNutritionComments.isEmpty() ? null : listNutritionComments;        
    }

    @Transactional(readOnly = true)
    public boolean isOwnerComment(Long commentId, Authentication authentication) {
        return nutritionCommentRepository.findById(commentId)
                .map(comment -> {
                    User user = comment.getUser();
                    return user != null && authentication.getName().equals(user.getEmail());
                })
                .orElse(false);
    }

    
    //@GetMapping("/nutritionComments/{commentId}/editcommentAdmin")
    public String editCommentAdmin(Model model, @PathVariable Long commentId) {
        NutritionComment comment = nutritionCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            Nutrition nutrition = comment.getNutrition();
            return "redirect:/nutritionComments/" + nutrition.getId() + "/" + commentId + "/editcomment";
        } else {
            return "redirect:/admin";
        }
    }

    //@GetMapping("/nutritionComments/{commentId}/deleteAdmin")
    public String deleteCommentAdmin(Model model, @PathVariable Long commentId) {
        NutritionComment comment = nutritionCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            Nutrition nutrition = comment.getNutrition();
            nutritionCommentService.deleteCommentbyId(nutrition, commentId);
        }
        return "redirect:/admin";
    }
    
}
