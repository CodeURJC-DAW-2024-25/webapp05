package es.codeurjc.daw.alphagym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;

@Controller
public class DataBaseInit implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(new User("user1", "user1gmail,com", "123"));
    }
}