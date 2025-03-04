package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Service
public class TrainingService {


    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TrainingCommentRepository trainingCommentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TrainingCommentService trainingCommentService;

    public Training createTraining(Training training) {
        Training newTraining = new Training(training.getName(),training.getIntensity(),training.getDuration(),training.getGoal(),training.getDescription());
        //newTraining.setGymUser(user);
        if (training.getImage() != null && !training.getImage().equals("/images/emptyImage.png")) {
            newTraining.setImage(training.getImage());
        }
        trainingRepository.save(newTraining);
        return newTraining;
    }

    public List<Training> getAllTrainings(){
        List<Training> listTraining = trainingRepository.findAll();
        return listTraining.isEmpty() ? null : listTraining;
    }

    public Training getTraining(Long id){
        Optional<Training> theRoutine = trainingRepository.findById(id);
        if (theRoutine.isPresent()) {
            Training training = theRoutine.get();
            return training;
        } else {
            return null;
        }
    }

    public Training updateRoutine(Long routineId, Training training){//, GymUser user
        Optional<Training> theRoutine = trainingRepository.findById(routineId);
        if(theRoutine.isPresent()) {

            training.setId(routineId);
            training.setImage(theRoutine.get().getImage());
            //training.setGymUser(user);
            trainingRepository.save(training);
            return training;
        }

        return null;
    }

    public Training deleteRoutine(Long id){
        Optional<Training> theRoutine = trainingRepository.findById(id);
        if (theRoutine.isPresent()) {
            Training training = theRoutine.get();
            //User user = routine.getGymUser();
            //user.getListRoutine().remove(routine);
            trainingRepository.delete(training);
            return training;
        }
        return null;
    }
/*
    public Collection<Training> getUserTrainings(Long id){
        Optional<List<Training>> listTrainingUser = trainingRepository.findByUser(userService.getUser(id));
        if(listTrainingUser.isPresent()){
            return listTrainingUser.get();
        }
        return null;
    }
    */

}
