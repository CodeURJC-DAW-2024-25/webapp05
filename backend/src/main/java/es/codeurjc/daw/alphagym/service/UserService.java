package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import es.codeurjc.daw.alphagym.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    NutritionRepository nutritionRepository;
    @Autowired
    TrainingRepository trainingRepository;

    public User createUser(User user) {
        userRepository.save(user);
        return user;
    }

    public User getUser(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }
}
