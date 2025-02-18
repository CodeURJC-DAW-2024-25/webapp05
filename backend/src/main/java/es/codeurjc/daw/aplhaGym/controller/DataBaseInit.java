package es.codeurjc.daw.aplhaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.boot.CommandLineRunner;

import es.codeurjc.daw.aplhaGym.model.User;
import es.codeurjc.daw.aplhaGym.repository.UserRepository;

@Controller
public class DataBaseInit implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Override
    public void run(String... args) throws Exception {
        //repository.save(new User("Alicia Rodriguez", "aliciaRo@gmail,com" , "Calle Eugenia de Montijo 76", "1234"));
        //falta añadir un admin 
    }
}