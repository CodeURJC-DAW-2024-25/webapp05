package es.codeurjc.daw.alphagym.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.service.TrainingCommentService;
import es.codeurjc.daw.alphagym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class TrainingCommentController { 
      
    @Autowired
    private UserService userService;
    @Autowired
    private TrainingCommentService trainingCommentService;
    //@Autowired
    //private TrainingCommentRepository trainingCommentRepository;
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
    public String showAllTrainingComments(Model model, @PathVariable Long trainingId){
        model.addAttribute("comment", trainingCommentService.getTrainingComments(trainingId));
        return "comments";
    }

    @GetMapping("/trainingComments/{trainingId}/newComment")
    public String newComment(Model model, @PathVariable Long trainingId){
        return "newComment";
    }

    @GetMapping("/trainingComments/{trainingId}/{commentId}")
    public String showComment(Model model, @PathVariable Long trainingId, @PathVariable Long commentId){
        return "comment";
    }

    @PostMapping("/trainingComments/{trainingId}")
    public String createComment(Model model,@PathVariable Long trainingId, @RequestParam String commentTitle,@RequestParam String commentText){

        TrainingComment trainingComment = new TrainingComment(commentText,commentTitle);
        trainingComment.setTrainingId(trainingId); // Asociar el comentario con el entrenamiento
        trainingCommentService.createTrainingComment(trainingComment);

        return "redirect:/trainingComments/" + trainingId;
    }



}
