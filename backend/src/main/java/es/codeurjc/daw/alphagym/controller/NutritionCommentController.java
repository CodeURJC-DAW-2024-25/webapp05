package es.codeurjc.daw.alphagym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.service.NutritionCommentService;
import es.codeurjc.daw.alphagym.service.NutritionService;
import es.codeurjc.daw.alphagym.service.UserService;

@Controller
public class NutritionCommentController {
    
    @Autowired
    private NutritionCommentService nutritionCommentService;
    /*@Autowired
    private UserService userService;
    @Autowired
    private NutritionService nutritionService;
    @Autowired
    private NutritionCommentRepository nutritionCommentRepository;*/


    @GetMapping("/nutritionComments/{nutritionId}")
    public String showAllNutritionComments(Model model){
        model.addAttribute("comment",nutritionCommentService.getAllNutritionComments());

        return "comments";
    }

}
