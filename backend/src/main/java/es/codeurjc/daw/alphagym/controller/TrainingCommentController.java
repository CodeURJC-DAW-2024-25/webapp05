package es.codeurjc.daw.alphagym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.service.TrainingCommentService;
import es.codeurjc.daw.alphagym.service.TrainingService;
import es.codeurjc.daw.alphagym.service.UserService;


@Controller
public class TrainingCommentController {
     
    @Autowired
    public UserService userService;
    @Autowired
    public TrainingCommentService trainingCommentService;
    @Autowired
    public TrainingCommentRepository trainingCommentRepository;
    @Autowired
    public TrainingService trainingService;
    
    /*@GetMapping("/trainingComments/{trainingId}")
    public String showAllTrainingComments(Model model, @PathVariable Long trainingId){
        model.addAttribute("comments", trainingCommentService.getAllTrainingComments(trainingId));
        return "comments";
    }*/
}
