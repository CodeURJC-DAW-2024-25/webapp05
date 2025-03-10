package es.codeurjc.daw.alphagym.controller;

import java.security.Principal;
import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.service.TrainingCommentService;
import es.codeurjc.daw.alphagym.service.TrainingService;
import es.codeurjc.daw.alphagym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class TrainingCommentController {

    @Autowired
    private UserService userService;
    @Autowired
    private TrainingCommentService trainingCommentService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainingCommentRepository trainingCommentRepository;
    //@Autowired
    //private TrainingComment trainingComment;


    @ModelAttribute("user")
    public void addAttributes(Model model, HttpServletRequest request){

        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            Optional <User> user = userService.findByEmail(principal.getName()); //se usa getName porque asi se hace desde security
            if (user.isPresent()){
                if (user.get().isRole("USER")){
                    model.addAttribute("user", true);
                }
                if (user.get().isRole("ADMIN")){
                    model.addAttribute("admin", true);
                }
            }
            model.addAttribute("logged", true);
            model.addAttribute("userName", principal.getName());
        } else {
            model.addAttribute("logged", false);
        }

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
    }



    @GetMapping("/trainingComments/{trainingId}")
    public String showAllTrainingComments(Model model, @PathVariable Long trainingId, Principal principal){
        // model.addAttribute("comment", trainingCommentService.getTrainingComments(trainingId));
        model.addAttribute("training", trainingService.getTraining(trainingId));

        boolean isAdmin = false;
        Long userId = null;

        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                model.addAttribute("logged", true);
                isAdmin = user.get().isRole("ADMIN"); // Suponiendo que el usuario tiene un método isAdmin()
                userId = user.get().getId();
            }
        }
        List<TrainingComment> trainingComments = trainingCommentService.getTrainingComments(trainingId);
        List<Map<String, Object>> commentList = new ArrayList<>();

        for (TrainingComment trainingComment : trainingComments) {
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("id", trainingComment.getId());
            commentMap.put("name", trainingComment.getName());
            commentMap.put("description", trainingComment.getDescription());

            // Avoid NullPointerException if the comment doesn`t have author
            User commentUser = trainingComment.getUser();
            boolean canEdit = isAdmin || (commentUser != null && userId != null && commentUser.getId().equals(userId));

            commentMap.put("canEdit", canEdit);
            commentMap.put("isAdmin", isAdmin);
            commentList.add(commentMap);
        }

        model.addAttribute("comment", commentList);

        return "commentTraining";
    }

    @GetMapping("/trainingComments/{trainingId}/newComment")
    public String newComment(Model model, @PathVariable Long trainingId){
        return "newComment";
    }


    @PostMapping("/trainingComments/{trainingId}")
    public String createComment(Model model,@PathVariable Long trainingId, @RequestParam String commentTitle,@RequestParam String commentText, Principal principal){
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            TrainingComment trainingComment = new TrainingComment(commentText, commentTitle);
            Training training = trainingService.getTraining(trainingId);

            if (user.isPresent()) {
                if (user.get().isRole("ADMIN")) {
                    trainingCommentService.createTrainingComment(trainingComment, training, null);
                }else{
                    trainingCommentService.createTrainingComment(trainingComment, training, user.get());
                }
            }

        }
        return "redirect:/trainingComments/" + trainingId;
    }

    @GetMapping("/trainingComments/{trainingId}/{commentId}/delete")
    public String deleteComment(Model model, @PathVariable Long trainingId, @PathVariable Long commentId){
        Training training = trainingService.getTraining(trainingId);
        trainingCommentService.deleteCommentbyId(training, commentId);
        return "redirect:/trainingComments/" + trainingId;
    }

    @GetMapping("/trainingComments/{trainingId}/{commentId}/report")
    public String reportComment(Model model, @PathVariable Long trainingId, @PathVariable Long commentId){
        trainingCommentService.reportCommentbyId(commentId);
        return "redirect:/trainingComments/" + trainingId;
    }

    @GetMapping("/trainingComments/{trainingId}/{commentId}/editcomment")
    public String editComment(Model model, @PathVariable Long trainingId, @PathVariable Long commentId) {
        model.addAttribute("comment", trainingCommentService.getCommentById(commentId));
        return "editComment";
    }

    @PostMapping("/trainingComments/{trainingId}/{commentId}")
    public String updateComment(Model model, @PathVariable Long trainingId, @PathVariable Long commentId,
                                @RequestParam String commentTitle, @RequestParam String commentText) {
        TrainingComment trainingComment = trainingCommentService.getCommentById(commentId);
        if (trainingComment != null) {
            trainingComment.setDescription(commentText);
            trainingComment.setName(commentTitle);
            trainingCommentService.updateComment(trainingComment);
        }
        return "redirect:/trainingComments/" + trainingId;
    }

    @GetMapping("/trainingComments/{commentId}/unreport")
    public String unreportComment(Model model, @PathVariable Long commentId) {
        trainingCommentService.unreportCommentbyId(commentId);
        return "redirect:/admin";
    }

    @GetMapping("/trainingComments/{commentId}/editcommentAdmin")
    public String editCommentAdmin(Model model, @PathVariable Long commentId) {
        TrainingComment comment = trainingCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            Training training = comment.getTraining();
            return "redirect:/trainingComments/" + training.getId() + "/" + commentId + "/editcomment";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/trainingComments/{commentId}/deleteAdmin")
    public String deleteCommentAdmin(Model model, @PathVariable Long commentId) {
        TrainingComment comment = trainingCommentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            Training training = comment.getTraining();
            trainingCommentService.deleteCommentbyId(training, commentId);
        }
        return "redirect:/admin";
    }

    @GetMapping("/trainingComments/{trainingId}/moreComments")
    public String loadMoreComments2(Model model, @PathVariable Long trainingId, @RequestParam(defaultValue = "1") int page) {
        List<TrainingComment> comments = trainingCommentService.getPaginatedComments(trainingId, page, 10);
        model.addAttribute("comment", comments);
        return "fragments/commentsTrainingList";
    }

}