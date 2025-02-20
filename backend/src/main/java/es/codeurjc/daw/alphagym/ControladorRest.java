package es.codeurjc.daw.alphagym;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControladorRest {
    @GetMapping("/ControladorRest")
    public String hello() {

        return "Hello, world!";
    }

}
