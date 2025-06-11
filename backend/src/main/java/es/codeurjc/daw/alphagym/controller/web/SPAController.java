package es.codeurjc.daw.alphagym.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SPAController {
    @GetMapping({"/spa", "/spa/**/{path:[^\\.]*}", "/{path:spa[^\\.]*}", "/spa/", "/new", "/new/**/{path:[^\\.]*}", "/{path:spa[^\\.]*}"})
    public String redirect() {

        return "forward:/spa/index.html";

    }

}
