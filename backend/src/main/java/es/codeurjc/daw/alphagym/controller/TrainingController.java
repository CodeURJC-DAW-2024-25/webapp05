package es.codeurjc.daw.alphagym.controller;

import java.security.Principal;
import java.util.Optional;

import es.codeurjc.daw.alphagym.dtosEdit.Goal;
import es.codeurjc.daw.alphagym.dtosEdit.Intensity;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import es.codeurjc.daw.alphagym.service.TrainingCommentService;
import es.codeurjc.daw.alphagym.service.TrainingService;
import es.codeurjc.daw.alphagym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.ArrayList;
import java.util.List;

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
    public String showAllRoutines(Model model){
        model.addAttribute("trainings",trainingService.getAllTrainings());
        return "training";
    }


    @GetMapping("/trainings/{trainingId}")
    public String showRoutine(Model model, @PathVariable Long trainingId, Principal principal) {
        Training training = trainingService.getTraining(trainingId);

        if (training == null) {
            return "redirect:/trainings";
        }

        model.addAttribute("training", training);

        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                model.addAttribute("logged", true);
                model.addAttribute("admin", user.get().isRole("ADMIN")); // Agrega la variable "admin"
            }
        } else {
            model.addAttribute("logged", false);
            model.addAttribute("admin", false); // Si no está autenticado, no es admin
        }

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
        String originalIntensity = training.getIntensity();
        String originalGoal = training.getGoal();

        List <Intensity> intensities = new ArrayList<>();
        intensities.add(new Intensity("50%", "50%".equals(originalIntensity)));
        intensities.add(new Intensity("60%", "60%".equals(originalIntensity)));
        intensities.add(new Intensity("70%", "70%".equals(originalIntensity)));
        intensities.add(new Intensity("80%", "80%".equals(originalIntensity)));
        intensities.add(new Intensity("90%", "90%".equals(originalIntensity)));
        intensities.add(new Intensity("100%", "100%".equals(originalIntensity)));

        List <Goal> goals = new ArrayList<>();
        goals.add(new Goal("Increase weight", "Increase weight".equals(originalGoal)));
        goals.add(new Goal("Increase volume", "Increase volume".equals(originalGoal)));
        goals.add(new Goal("Lose weight", "Lose weight".equals(originalGoal)));

        model.addAttribute("intensities", intensities);
        model.addAttribute("goals", goals);


        if(training == null){
            return "redirect:/trainings";
        }
        model.addAttribute("training",training);
        //model.addAttribute("userId",userId);
        return "editRoutine";
    }
    @PostMapping("/trainings/editTraining/{trainingId}")
    public String editRoutinePost(@ModelAttribute Training training,@PathVariable Long trainingId){//, @RequestParam("userId") Long userId
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
    public  String deleteRoutinePost(@PathVariable Long trainingId){//, @RequestParam("userId") Long userId
        trainingService.deleteRoutine(trainingId);
        //User user = userService.getGymUser(userId);
        return "redirect:/trainings";
    }


}

