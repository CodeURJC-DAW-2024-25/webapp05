package es.codeurjc.daw.alphagym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.service.NutritionCommentService;
import es.codeurjc.daw.alphagym.service.UserService;


@Controller
public class NutritionCommentController {
    
    @Autowired
    private NutritionCommentService nutritionCommentService;
    @Autowired
    private UserService userService;
    @Autowired
    private NutritionCommentRepository nutritionCommentRepository;


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

}
