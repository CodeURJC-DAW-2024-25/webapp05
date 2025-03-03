package es.codeurjc.daw.alphagym.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import es.codeurjc.daw.alphagym.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DataBaseInit {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingRepository  trainingRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() throws IOException {

        //Users Examples
        User admin = new User("admin@admin.com", "admin", passwordEncoder.encode("adminpass"), "ADMIN", "USER");
        userRepository.save(admin);

        User user = new User("user", "user@user.com", passwordEncoder.encode("pass"), "USER");
        userRepository.save(user);

    }
    
}
