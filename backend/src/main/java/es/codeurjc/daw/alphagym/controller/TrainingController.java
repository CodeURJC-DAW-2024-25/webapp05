package es.codeurjc.daw.alphagym.controller;

import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import es.codeurjc.daw.alphagym.service.TrainingCommentService;
import es.codeurjc.daw.alphagym.service.TrainingService;
import es.codeurjc.daw.alphagym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    //esto guardpalo para despues 
/* 
    @GetMapping("/routines")
    public String showAllRoutines(Model model, @RequestParam("userId") Long userId){
        model.addAttribute("routines",trainingService.getAllTrainings(userId));
        User user = userService.getUser(userId);
        //De momento lo siguiente no tiene sentido porque aunque no haya user (usuario no registrado) se le deben mostrar todas las rutinas
        if(user != null){
            model.addAttribute("userId",user.getId());
            return "training";
        }
        return "redirect:/training";
    }
*/

    //  @GetMapping("/routines/{routineId}")

    //  @GetMapping("/routines/createRoutine")

    //  @PostMapping("/routines/createRoutine")

    //  @GetMapping("/routines/editRoutine/{routineId}")

    //  @PostMapping("/routines/editRoutine/{routineId}")

    //  @GetMapping("/routines/delete/{routineId}")
}

