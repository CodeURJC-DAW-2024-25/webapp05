package es.codeurjc.daw.alphagym.controller;

import java.security.Principal;
import java.util.Optional;

import com.samskivert.mustache.Mustache;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import es.codeurjc.daw.alphagym.service.TrainingCommentService;
import es.codeurjc.daw.alphagym.service.TrainingService;
import es.codeurjc.daw.alphagym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;

import java.io.IOException;
import java.io.Writer;
import java.util.function.BiFunction;

import java.util.List;
import java.util.function.Function;

@Controller
public class TrainingController {

    @Autowired
    private TrainingService trainingService;
    @Autowired
    private UserService userService;
    @Autowired
    private TrainingCommentService trainingCommentService;
    @Autowired
    private TrainingRepository trainingRepository;

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

    @GetMapping("/trainings")
    public String showAllRoutines(Model model){//,  @RequestParam("userId") Long userId){
        model.addAttribute("trainings",trainingService.getAllTrainings());
        //User user = userService.getUser(userId);
        //De momento lo siguiente no tiene sentido porque aunque no haya user (usuario no registrado) se le deben mostrar todas las rutinas
        /*if(trainingService.getAllTrainings() != null){
           // model.addAttribute("userId",user.getId());
            return "training";
        }*/
        return "training";
    }


    @GetMapping("/trainings/{trainingId}")
    public String showRoutine(Model model, @PathVariable Long trainingId){
        Training training = trainingService.getTraining(trainingId);
        if(training == null){
            return "redirect:/trainings";
        }

        model.addAttribute("training",training);

        return "showRoutine";
    }

    @GetMapping("/trainings/createRoutine")
    public String createRoutine(Model model){//, @RequestParam("userId") Long userId
        model.addAttribute("training",new Training());
        //User user = userService.getGymUser(userId);
        //if(user != null){
            //model.addAttribute("userId",user.getId());
            return "newRoutine";
       // }
       // return "redirect:/Login";
    }
    @PostMapping("/trainings/createRoutine")
    public String createRoutinePost(@ModelAttribute Training training){//, @RequestParam("userId") Long userId
        //User user = userService.getGymUser(userId);
        //if (user != null){ aqui iria logica de user
            //training.setUser(user);
            trainingService.createTraining(training);
            return "redirect:/trainings";
        //}
        //return "redirect:/trainings";
    }


    @GetMapping("/trainings/editTraining/{trainingId}")
    public String editRoutine(Model model, @PathVariable Long trainingId){//, @RequestParam("userId") Long userId
        Training training = trainingService.getTraining(trainingId);
        //User user = userService.getUser(userId);
//        if(user == null){
//            return "redirect:/Login";
//        }
        if(training == null){
            return "redirect:/trainings";
        }
        model.addAttribute("training",training);
        //model.addAttribute("userId",userId);
        return "editRoutine";
    }
    @PostMapping("/trainings/editTraining/{trainingId}")
    public String editRoutinePost(@ModelAttribute Training training, @PathVariable Long trainingId){//, @RequestParam("userId") Long userId
        //GymUser user = userService.getGymUser(userId);
        try {
            trainingService.updateRoutine(trainingId, training);

            return "redirect:/trainings/" + trainingId;
        } catch (Exception e) {
            // Manejar la excepción, por ejemplo, registrar el error y mostrar un mensaje al usuario
            e.printStackTrace(); // Para depuración, considera usar un logger
            return "redirect:/trainings/editRoutine/" + trainingId + "?error=true"; // Redirigir con un parámetro de error
        }
    }

    @GetMapping("/trainings/delete/{trainingId}")
    public  String deleteRoutinePost(@PathVariable Long trainingId, @RequestParam("userId") Long userId){
        trainingService.deleteRoutine(trainingId);
        //User user = userService.getGymUser(userId);
        return "redirect:/trainings";
    }


}

