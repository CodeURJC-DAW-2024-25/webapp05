package es.codeurjc.daw.alphagym.controller;

import java.security.Principal;
import java.sql.SQLException;
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
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.multipart.MultipartFile;

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
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName()); //se usa getName porque asi se hace desde security
            if (user.isPresent()) {
                if (user.get().isRole("USER")) {
                    model.addAttribute("user", true);
                }
                if (user.get().isRole("ADMIN")) {
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
    public String showAllRoutines(Model model, Principal principal) {
        model.addAttribute("trainings", trainingService.getAllTrainings());
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                model.addAttribute("logged", true);
            }
        }
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
                Boolean isAdmin = user.get().isRole("ADMIN");
                // Evitar NullPointerException si la rutina no tiene usuario asignado
                Boolean canEdit = isAdmin || (training.getUser() != null && training.getUser().getId().equals(user.get().getId()));
                boolean isSubscribed = user.get().getTrainings().contains(training);

                model.addAttribute("subscribed", isSubscribed);
                model.addAttribute("logged", true);
                model.addAttribute("admin", user.get().isRole("ADMIN")); // add if logged user is "admin"
                model.addAttribute("canEdit", canEdit);
            }
        } else {
            model.addAttribute("logged", false);
            model.addAttribute("admin", false); // if it`s not logged he cant be "admin"
        }

        return "showRoutine";
    }

    @GetMapping("/trainings/subscribe/{trainingId}")
    public String subscribeToRoutine(@PathVariable Long trainingId, Principal principal) {
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                trainingService.subscribeTraining(trainingId, user.get());
            }
        }
        return "redirect:/trainings/" + trainingId;
    }

    @GetMapping("/trainings/unsubscribe/{trainingId}")
    public String unsubscribeFromRoutine(@PathVariable Long trainingId, Principal principal) {
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                trainingService.unsubscribeTraining(trainingId, user.get());
            }
        }
        return "redirect:/trainings/" + trainingId;
    }

    @GetMapping("/trainings/createRoutine")
    public String createRoutine(Model model) {
        model.addAttribute("training", new Training());
        return "newRoutine";
    }

    @PostMapping("/trainings/createRoutine")
    public String createRoutinePost(@ModelAttribute Training training, Principal principal) {
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                if (user.get().isRole("ADMIN")) {
                    trainingService.createTraining(training, null);
                } else {
                    trainingService.createTraining(training, user.get());
                }
                return "redirect:/trainings";
            }
        }
        return "redirect:/trainings";
    }


    @GetMapping("/trainings/editTraining/{trainingId}")
    public String editRoutine(Model model, @PathVariable Long trainingId) {
        Training training = trainingService.getTraining(trainingId);
        String originalIntensity = training.getIntensity();
        String originalGoal = training.getGoal();

        List<Intensity> intensities = new ArrayList<>();
        intensities.add(new Intensity("50%", "50%".equals(originalIntensity)));
        intensities.add(new Intensity("60%", "60%".equals(originalIntensity)));
        intensities.add(new Intensity("70%", "70%".equals(originalIntensity)));
        intensities.add(new Intensity("80%", "80%".equals(originalIntensity)));
        intensities.add(new Intensity("90%", "90%".equals(originalIntensity)));
        intensities.add(new Intensity("100%", "100%".equals(originalIntensity)));

        List<Goal> goals = new ArrayList<>();
        goals.add(new Goal("Increase weight", "Increase weight".equals(originalGoal)));
        goals.add(new Goal("Increase volume", "Increase volume".equals(originalGoal)));
        goals.add(new Goal("Lose weight", "Lose weight".equals(originalGoal)));

        model.addAttribute("intensities", intensities);
        model.addAttribute("goals", goals);


        if (training == null) {
            return "redirect:/trainings";
        }
        model.addAttribute("training", training);
        return "editRoutine";
    }

    @PostMapping("/trainings/editTraining/{trainingId}")
    public String editRoutinePost(@ModelAttribute Training training, @PathVariable Long trainingId, @RequestParam MultipartFile imageFile, Model model, Principal principal) {
        try {
            if (principal != null) {
                Optional<User> user = userService.findByEmail(principal.getName());
                if (imageFile != null && !imageFile.isEmpty()) {
                    training.setImgTraining(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
                    training.setImage(true);
                }
                trainingService.updateRoutine(trainingId, training, user.get());

                return "redirect:/trainings/" + trainingId;
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ha ocurrido un error.");
            return "redirect:/trainings/editRoutine/" + trainingId + "?error=true"; // Redirigir con un parámetro de error
        }

        return null;
    }


    @GetMapping("/trainings/delete/{trainingId}")
    public  String deleteRoutinePost(@PathVariable Long trainingId){
        trainingService.deleteRoutine(trainingId);
        //User user = userService.getGymUser(userId);
        return "redirect:/trainings";
    }

    @GetMapping("/trainings/deleteFromList/{trainingId}")
    public  String deleteRoutineFromListPost(@PathVariable Long trainingId, Principal principal){
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                trainingService.unsubscribeTraining(trainingId, user.get());
                return "redirect:/account";
            }
        }
        return null;
    }

    @GetMapping("/editTraining/image/{trainingId}")
    public ResponseEntity<Object> downloadImage(@PathVariable Long trainingId, Principal principal) throws SQLException {
        if (principal != null) {

            Optional<User> user = userService.findByEmail(principal.getName());
            Training training = trainingService.getById(trainingId);

            if (user.isPresent() && training.getImgTraining() != null) {

                Resource file = new InputStreamResource(training.getImgTraining().getBinaryStream());

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .contentLength(user.get().getImg_user().length())
                        .body(file);
            }
        }
        return ResponseEntity.notFound().build();
    }




}

