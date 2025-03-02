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

import es.codeurjc.daw.alphagym.model.NutritionComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.service.NutritionCommentService;
import es.codeurjc.daw.alphagym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class NutritionCommentController {
    
    @Autowired
    private NutritionCommentService nutritionCommentService;
    @Autowired
    private UserService userService;
    //@Autowired
    //private NutritionCommentRepository nutritionCommentRepository;


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

    @GetMapping("/nutritionComments/{nutritionId}")
    public String showAllNutritionComments(Model model){
        model.addAttribute("comment",nutritionCommentService.getAllNutritionComments());
        return "comments";
    }

    @GetMapping("/nutritionComments/{nutritionId}/newComment")
    public String newComment(Model model, @PathVariable Long nutritionId){
        return "newComment";
    }

    @GetMapping("/nutritionComments/{nutritionId}/{commentId}")
    public String showComment(Model model, @PathVariable Long nutritionId, @PathVariable Long commentId){
        return "comment";
    }

    @PostMapping("/nutritionComments/{nutritionId}")
    public String createComment(Model model,@PathVariable Long nutritionId, @RequestParam String commentTitle,@RequestParam String commentText){

        NutritionComment nutritionComment = new NutritionComment(commentText,commentTitle);
        nutritionComment.setNutritionId(nutritionId); // Asociar el comentario con el entrenamiento
        nutritionCommentService.createNutritionComment(nutritionComment);

        return "redirect:/nutritionComments/" + nutritionId;
    }
}
