package es.codeurjc.daw.alphagym.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import es.codeurjc.daw.alphagym.model.NutritionComment;
import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.service.NutritionCommentService;
import es.codeurjc.daw.alphagym.service.NutritionService;
import es.codeurjc.daw.alphagym.service.UserService;

@Controller
public class NutritionController {

    @Autowired
    private NutritionService nutritionService;
    @Autowired
    private UserService userService;
    @Autowired
    private NutritionCommentService nutritionCommentService;

    
}
