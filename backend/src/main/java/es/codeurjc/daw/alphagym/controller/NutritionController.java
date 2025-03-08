package es.codeurjc.daw.alphagym.controller;



import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.codeurjc.daw.alphagym.dtosEdit.Goal;
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
    public String showAllDiets(Model model){
        model.addAttribute("nutritions", nutritionService.getAllNutritions());
        return "nutrition";
    }

    @GetMapping("/nutritions/{id}")
    public String detailsNutrition(Model model, @PathVariable Long id, Principal principal){
        Nutrition nutrition = nutritionService.getNutrition(id);
        if (nutrition == null) {
            return "redirect:/nutritions";
        }
        model.addAttribute("nutrition", nutrition);
        
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                Boolean isAdmin = user.get().isRole("ADMIN");
                 // Evitar NullPointerException si la rutina no tiene usuario asignado
                 Boolean canEdit = isAdmin || (nutrition.getUser() != null && nutrition.getUser().getId().equals(user.get().getId()));
                 boolean isSubscribed = user.get().getNutritions().contains(nutrition);
 
                model.addAttribute("subscribed", isSubscribed);
                model.addAttribute("logged", true);
                model.addAttribute("admin", user.get().isRole("ADMIN")); // Agrega la variable "admin"
                model.addAttribute("canEdit", canEdit);
            }
        } else {
            model.addAttribute("logged", false);
            model.addAttribute("admin", false); // Si no está autenticado, no es admin
        }

        return "showDiet";
    }

    @GetMapping("/nutritions/subscribe/{id}")
     public String subscribeToDiet(@PathVariable Long id, Principal principal) {
         if (principal != null) {
             Optional<User> user = userService.findByEmail(principal.getName());
             if (user.isPresent()) {
                 nutritionService.subscribeNutrition(id, user.get());
             }
         }
         return "redirect:/nutritions/" + id;
     }
 
     @GetMapping("/nutritions/unsubscribe/{id}")
     public String unsubscribeFromDiet(@PathVariable Long id, Principal principal) {
         if (principal != null) {
             Optional<User> user = userService.findByEmail(principal.getName());
             if (user.isPresent()) {
                 nutritionService.unsubscribeNutrition(id, user.get());
             }
         }
         return "redirect:/nutritions/" + id;
     }

    @GetMapping("/nutritions/newNutrition")
    public String createNutrition(Model model ) {
        model.addAttribute("nutrition",new Nutrition());
        return "newDiet";
    }

    @PostMapping("/nutritions/newNutrition")
    public String createNutritionPost(@ModelAttribute Nutrition nutrition, Principal principal){
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                if (user.get().isRole("ADMIN")) {
                    nutritionService.createNutrition(nutrition, null);
                } else {
                    nutritionService.createNutrition(nutrition, user.get());
                }
                return "redirect:/trainings";
            }
        }
        return "redirect:/nutritions";
    }

    @GetMapping("/nutritions/editNutrition/{id}")
    public String editDiet(Model model, @PathVariable Long id) {
        Nutrition nutrition = nutritionService.getNutrition(id);
        String originalGoal = nutrition.getGoal();

        List<Goal> goals = new ArrayList<>();
        goals.add(new Goal("Maintain weight", "Maintain weight".equals(originalGoal)));
        goals.add(new Goal("Increase weight", "Increase weight".equals(originalGoal)));
        goals.add(new Goal("Lose weight", "Lose weight".equals(originalGoal)));
        model.addAttribute("goals", goals);

        if (nutrition == null){
            return "redirect:/nutritions";
        }

        model.addAttribute("nutrition",nutrition);
        return "editDiet";
    }

    @PostMapping("/nutritions/editNutrition/{id}")
    public String editDietPost(@ModelAttribute Nutrition nutrition, @PathVariable Long id, Model model, Principal principal) {
        try {
            if (principal != null) {
                Optional<User> user = userService.findByEmail(principal.getName());
                nutritionService.editDiet(id, nutrition, user.get());
                return "redirect:/nutritions/" + id;
            }
        } catch (Exception e) {
            // Manejar la excepción, por ejemplo, registrar el error y mostrar un mensaje al usuario
            e.printStackTrace(); // Para depuración, considera usar un logger
            model.addAttribute("error", "Ha ocurrido un error.");
            return "redirect:/nutritions/editDiet/" + id + "?error=true"; // Redirigir con un parámetro de error
        }

        return null;
    }

    @GetMapping("/nutritions/delete/{id}")
    public  String deleteDietPost(@PathVariable Long id){
        nutritionService.deleteDiet(id);
        //User user = userService.getUser(userId);
        return "redirect:/nutritions";
    }

    @GetMapping("/nutritions/deleteFromList/{id}")
    public  String deleteDietFromListPost(@PathVariable Long id, Principal principal){
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                nutritionService.unsubscribeNutrition(id, user.get());
                return "redirect:/account";
            }
        }
        return null;
    }

    
}
