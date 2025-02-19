package es.codeurjc.daw.alphagym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;

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