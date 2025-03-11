package es.codeurjc.daw.alphagym.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.service.NutritionService;
import es.codeurjc.daw.alphagym.service.TrainingService;
import es.codeurjc.daw.alphagym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;


public class SearchController {
    @Autowired
    private UserService userService;

    @Autowired
    private NutritionService nutritionService;

    @Autowired
    private TrainingService trainingService;

    @GetMapping("/search")     
    public String searchProducts(@RequestParam(required = false) String search, Model model, HttpServletRequest request) {

        String principal = request.getUserPrincipal().getName();
        Optional<User> admin = userService.findByEmail(principal);

        if (admin.isPresent()) {

            model.addAttribute("searchQuery", search != null ? search : "");

            //Search for trainings and nutrition related to the search term
            List<Training> trainings = trainingService.findByName(search);
            List<Nutrition> nutritions = nutritionService.findByName(search);

            model.addAttribute("trainings", trainings);
            model.addAttribute("nutritions", nutritions);

            return "searchResults";
        }
        return "error"; 
    }
    
}
