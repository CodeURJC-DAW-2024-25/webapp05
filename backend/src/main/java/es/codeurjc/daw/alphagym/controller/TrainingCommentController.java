package es.codeurjc.daw.alphagym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.service.TrainingCommentService;
import es.codeurjc.daw.alphagym.service.UserService;


@Controller
public class TrainingCommentController { 
      
    /*
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
    }
    */
    
    @Autowired
    private UserService userService;
    @Autowired
    private TrainingCommentService trainingCommentService;
    @Autowired
    private TrainingCommentRepository trainingCommentRepository;

    
    @GetMapping("/trainingComments/{trainingId}")
    public String showAllTrainingComments(Model model, @PathVariable Long trainingId){
        model.addAttribute("comment", trainingCommentService.getAllTrainingComments(trainingId));
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
    /*
    @PostMapping("/trainingComments/{trainingId}")
    public String createComment(Model model,@PathVariable Long trainingId){
        model.addAttribute("comment", trainingCommentService.getAllTrainingComments(trainingId));
        return "comments";
    }
    */




}
