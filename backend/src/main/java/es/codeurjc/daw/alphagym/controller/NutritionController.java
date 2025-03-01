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
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
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
    @Autowired
    private NutritionRepository nutritionRepository;


    @GetMapping("/nutritions")
    public String showAllDiets(Model model){//,  @RequestParam("userId") Long userId){
        model.addAttribute("nutritions",nutritionService.getAllNutritions());
        //User user = userService.getUser(userId);
        //De momento lo siguiente no tiene sentido porque aunque no haya user (usuario no registrado) se le deben mostrar todas las rutinas
        /*if(trainingService.getAllTrainings() != null){
           // model.addAttribute("userId",user.getId());
            return "training";
        }*/
        return "nutrition";
    }

    @GetMapping("/nutritions/{id}")
    public String detailsNutrition(Model model, @PathVariable Long id/*, @RequestParam("userId") Long userId*/){
        Nutrition nutrition = nutritionService.getNutrition(id);
        if (nutrition == null) {
            return "redirect:/nutrition";
        }
        model.addAttribute("nutrition", nutrition);
        /*User user = userService.getUser(userId);
        if(user != null){
            model.addAttribute("userId",user.getId());
            return "detailsNutrition";
        }*/
        return "showDiet";
    }

    
}
