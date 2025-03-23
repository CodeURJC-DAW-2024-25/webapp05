package es.codeurjc.daw.alphagym.service;

import java.util.Collection;
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

import es.codeurjc.daw.alphagym.dto.NutritionCommentDTO;
import es.codeurjc.daw.alphagym.dto.NutritionCommentMapper;
import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.NutritionComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;

@Service
public class NutritionCommentService {

    @Autowired
    private NutritionCommentRepository nutritionCommentRepository;
    @Autowired
    private NutritionCommentMapper nutritionCommentMapper;

    public List<NutritionComment> getAllNutritionComments() {
        List<NutritionComment> listNutritionComment = nutritionCommentRepository.findAll();
        return listNutritionComment.isEmpty() ? null : listNutritionComment;
    }

    public List<NutritionComment> getNutritionComments(Long nutritionId) {
        List<NutritionComment> listNutritionComments = getPaginatedComments(nutritionId, 0, 10);
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
        return new Long[] { reported, notReported };
    }

    public List<NutritionComment> getPaginatedComments(Long nutritionId, int page, int limit) {
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

    // @GetMapping("/nutritionComments/{commentId}/editcommentAdmin")
    public String editCommentAdmin(Model model, @PathVariable Long commentId) {
        NutritionComment comment = nutritionCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            Nutrition nutrition = comment.getNutrition();
            return "redirect:/nutritionComments/" + nutrition.getId() + "/" + commentId + "/editcomment";
        } else {
            return "redirect:/admin";
        }
    }

    // @GetMapping("/nutritionComments/{commentId}/deleteAdmin")
    public String deleteCommentAdmin(Model model, @PathVariable Long commentId) {
        NutritionComment comment = nutritionCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            Nutrition nutrition = comment.getNutrition();
            deleteCommentbyId(nutrition, commentId);
        }
        return "redirect:/admin";
    }

    /*
     * ADD DTOs METHODS
     */
    public Collection<NutritionCommentDTO> getAllNutritionCommentsDTO() {
        return nutritionCommentMapper.toDTOs(nutritionCommentRepository.findAll());
    }

    public Collection<NutritionCommentDTO> getNutritionCommentsDTO(Long nutritionId) {
        return nutritionCommentMapper.toDTOs(nutritionCommentRepository.findByNutritionId(nutritionId));
    }

    public List<NutritionCommentDTO> getPaginatedCommentsDTO(Long nutritionId, int page, int limit) {
        return nutritionCommentRepository
                .findByNutritionId(nutritionId, PageRequest.of(page, limit))
                .map(nutritionCommentMapper::toDTO)
                .toList();
    }

    public NutritionCommentDTO createNutritionComment(NutritionCommentDTO nutritionCommentDTO) {
        NutritionComment nutritionComment = toDomain(nutritionCommentDTO);
        nutritionComment = nutritionCommentRepository.save(nutritionComment);
        return toDTO(nutritionComment);
    }

    public void replaceNutritionComment(NutritionCommentDTO nutritionCommentDTO) {
        nutritionCommentRepository.save(toDomain(nutritionCommentDTO));
    }

    /*
     * public NutritionCommentDTO createNutritionCommentDTO(NutritionCommentDTO
     * nutritionCommentDTO, Nutrition nutrition, User user) {
     * // Convertir DTO a entidad
     * NutritionComment nutritionComment = toDomain(nutritionCommentDTO);
     * 
     * // Llamar al método original para asignar user y nutrition
     * createNutritionComment(nutritionComment, nutrition, user);
     * 
     * // Retornar el DTO del comentario guardado
     * return toDTO(nutritionComment);
     * }
     */

    /*
     * public NutritionCommentDTO createNutritionCommentDTO(NutritionCommentDTO
     * nutritionCommentDTO) {
     * NutritionComment nutritionComment = toDomain(nutritionCommentDTO);
     * nutritionComment = nutritionCommentRepository.save(nutritionComment);
     * return toDTO(nutritionComment);
     * }
     * public NutritionCommentDTO getCommentByIdDTO(Long commentId) {
     * return toDTO(nutritionCommentRepository.findById(commentId).orElse(null));
     * }
     * public void updateCommentDTO(NutritionCommentDTO nutritionCommentDTO) {
     * nutritionCommentRepository.save(toDomain(nutritionCommentDTO));
     * }
     * public void deleteCommentbyIdDTO(Long commentId) {
     * nutritionCommentRepository.deleteById(commentId);
     * }
     * public void reportCommentbyIdDTO(Long commentId) {
     * NutritionComment comment =
     * nutritionCommentRepository.findById(commentId).orElse(null);
     * if (comment != null) {
     * comment.setIsNotified(true);
     * nutritionCommentRepository.save(comment);
     * }
     * }
     * public void unreportCommentbyIdDTO(Long commentId) {
     * NutritionComment comment =
     * nutritionCommentRepository.findById(commentId).orElse(null);
     * if (comment != null) {
     * comment.setIsNotified(false);
     * nutritionCommentRepository.save(comment);
     * }
     * }
     * public Long[] getReportAmmmountsDTO() {
     * Long reported = nutritionCommentRepository.countByIsNotified(true);
     * Long notReported = nutritionCommentRepository.countByIsNotified(false);
     * return new Long[] {reported, notReported};
     * }
     * public Collection<NutritionCommentDTO> getReportedCommentsDTO() {
     * return
     * nutritionCommentMapper.toDTOs(nutritionCommentRepository.findByIsNotified(
     * true));
     * }
     * public Collection<NutritionCommentDTO> getPaginatedCommentsDTO(Long
     * nutritionId, int page, int limit){
     * return nutritionCommentMapper.toDTOs(getPaginatedComments(nutritionId, page,
     * limit));
     * }
     * public boolean isOwnerCommentDTO(Long commentId, Authentication
     * authentication) {
     * return nutritionCommentRepository.findById(commentId)
     * .map(comment -> {
     * User user = comment.getUser();
     * return user != null && authentication.getName().equals(user.getEmail());
     * })
     * .orElse(false);
     * }
     * public void createNutritionCommentDTO(NutritionCommentDTO
     * nutritionCommentDTO, Nutrition nutrition, User user) {
     * NutritionComment nutritionComment = toDomain(nutritionCommentDTO);
     * nutritionComment.setUser(user);
     * nutritionComment.setNutrition(nutrition);
     * nutritionComment = nutritionCommentRepository.save(nutritionComment);
     * nutrition.getComments().add(nutritionComment);
     * }
     * public void deleteCommentbyIdDTO(Nutrition nutrition, Long commentId) {
     * NutritionComment comment =
     * nutritionCommentRepository.findById(commentId).orElse(null);
     * if (comment != null) {
     * nutrition.getComments().remove(comment);
     * }
     * nutritionCommentRepository.deleteById(commentId);
     * }
     * public void updateCommentDTO(NutritionCommentDTO nutritionCommentDTO,
     * Nutrition nutrition) {
     * NutritionComment comment = toDomain(nutritionCommentDTO);
     * comment.setNutrition(nutrition);
     * nutritionCommentRepository.save(comment);
     * }
     */
    // Send to API
    public NutritionCommentDTO toDTO(NutritionComment nutritionComment) {
        return nutritionCommentMapper.toDTO(nutritionComment);
    }

    // Return a comment List to API
    public Collection<NutritionCommentDTO> toDTOs(Collection<NutritionComment> nutritionComments) {
        return nutritionCommentMapper.toDTOs(nutritionComments);
    }

    // Data which comes from API result converted to the expected structure in the
    // backend
    public NutritionComment toDomain(NutritionCommentDTO nutritionCommentDTO) {
        return nutritionCommentMapper.toDomain(nutritionCommentDTO);
    }

}
