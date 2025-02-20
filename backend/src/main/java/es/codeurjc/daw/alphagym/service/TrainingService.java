package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
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

    public Training createTraining(Training training, User user) {
        Training training1 = new Training(training.getTrainingName(),training.getDuration(),training.getIntensity(),training.getDescription(), training.getImage(), training.getGoal());
        trainingRepository.save(training1);
        return training1;
    }

    public Collection<Training> getAllTrainings(Long id){
        Optional<List<Training>> listTrainingUser = trainingRepository.findByUser(userService.getUser(id));
        if(listTrainingUser.isPresent()){
            return listTrainingUser.get();
        }
        return null;
    }

}
