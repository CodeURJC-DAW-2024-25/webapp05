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
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.service.NutritionCommentService;
import es.codeurjc.daw.alphagym.service.NutritionService;
import es.codeurjc.daw.alphagym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

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
            return "redirect:/nutritions";
        }
        model.addAttribute("nutrition", nutrition);
        /*User user = userService.getUser(userId);
        if(user != null){
            model.addAttribute("userId",user.getId());
            return "detailsNutrition";
        }*/
        return "showDiet";
    }

    @GetMapping("/nutritions/newDiet")
    public String createNutrition(Model model /*,@RequestParam("userId") Long userId*/ ) {
        model.addAttribute("nutricion",new Nutrition());
        /*User user = userService.getUser(userId);
        if (user != null){
            model.addAttribute("userId",user.getId());
            return "createNutrition";
        }*/
        return "newDiet";
    }

    @PostMapping("/nutritions/newDiet")
    public String createNutritionPost(@ModelAttribute Nutrition nutrition/* , @RequestParam("userId") Long userId*/){
        /*User user = userService.getUser(userId);
        if (user != null){
            nutrition.setGymUser(user);
            Nutrition nut = nutritionService.createNutrition(nutrition,user);
            return "redirect:/ListFoods?nutritionId=" + nut.getId();
        }*/
        nutritionService.createNutrition(nutrition);
        return "redirect:/nutritions";
    }

    @GetMapping("/nutritions/editDiet/{id}")
    public String editDiet(Model model, @PathVariable Long id/* , @RequestParam("userId") Long userId*/) {
        Nutrition nutrition = nutritionService.getNutrition(id);
        if(nutrition == null){
            return "redirect:/nutritions";
        }
        model.addAttribute("nutrition",nutrition);
        //model.addAttribute("userId",userId);
        return "editDiet";
    }

    @PostMapping("/nutritions/editDiet/{id}")
    public String editDietPost(@ModelAttribute Nutrition nutrition, @PathVariable Long id){//, @RequestParam("userId") Long userId
        //User user = userService.getUser(userId);
        try {
            nutritionService.editDiet(id, nutrition);

            return "redirect:/nutritions/" + id;
        } catch (Exception e) {
            // Manejar la excepción, por ejemplo, registrar el error y mostrar un mensaje al usuario
            e.printStackTrace(); // Para depuración, considera usar un logger
            return "redirect:/nutritions/editDiet/" + id + "?error=true"; // Redirigir con un parámetro de error
        }
    }

    @GetMapping("/nutritions/delete/{id}")
    public  String deleteDietPost(@PathVariable Long id /*@RequestParam("userId") Long userId*/){
        nutritionService.deleteDiet(id);
        //User user = userService.getGymUser(userId);
        return "redirect:/nutritions";
    }


    
}
