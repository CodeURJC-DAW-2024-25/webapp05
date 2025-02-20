package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;
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
        Training training1 = new Training(training.getName(),training.getTitle(),training.getDescription(), training.getImage());
        trainingRepository.save(training);
        return training1;
    }
}
