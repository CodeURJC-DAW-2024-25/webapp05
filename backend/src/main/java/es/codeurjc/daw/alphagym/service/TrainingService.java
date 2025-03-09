package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import es.codeurjc.daw.alphagym.repository.UserRepository;
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
    private UserRepository userRepository;
    @Autowired
    private TrainingCommentService trainingCommentService;



    public Training createTraining(Training training, User user) {
        Training newTraining = new Training(training.getName(),training.getIntensity(),training.getDuration(),training.getGoal(),training.getDescription());
        newTraining.setUser(user);
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

    public Training updateRoutine(Long routineId, Training training, User user){
        Optional<Training> theRoutine = trainingRepository.findById(routineId);
        if(theRoutine.isPresent()) {
            training.setId(routineId);
            training.setImage(theRoutine.get().getImage());
            if (theRoutine.get().getUser()!=null){
                training.setUser(user);
            }
            trainingRepository.save(training);
            return training;
        }

        return null;
    }

    public Training deleteRoutine(Long id){
        Optional<Training> theRoutine = trainingRepository.findById(id);
        if (theRoutine.isPresent()) {
            Training training = theRoutine.get();

            List<User> usersWithTraining = userRepository.findByTrainingsContaining(training);
            for (User user : usersWithTraining) {
                user.getTrainings().remove(training);
                userRepository.save(user); // Guardar los cambios en el usuario ya que no hay mapped by cascade
            }

            List<TrainingComment> trainingComments = trainingCommentRepository.findByTrainingId(id);

            for (TrainingComment trainingComment : trainingComments) {
                trainingCommentRepository.delete(trainingComment);

            }
            trainingRepository.delete(training);
            return training;
        }
        return null;
    }

    public void subscribeTraining(Long trainingId , User user) {
        Optional<Training> training = trainingRepository.findById(trainingId);
        if (training.isPresent()) {
            user.getTrainings().add(training.get());
            userRepository.save(user);
        }
    }

    public void unsubscribeTraining(Long trainingId, User user) {
        Optional<Training> training = trainingRepository.findById(trainingId);
        if (training.isPresent()) {
            user.getTrainings().remove(training.get());
            userRepository.save(user);
        }
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
