package es.codeurjc.daw.aplhaGym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NewCommentController {
    @PostMapping("/comments")
    public String comments(Model model, @RequestParam String title, @RequestParam String comment) {
        // Agregar el título y comentario al modelo para que se muestren en
        // comments.html
        model.addAttribute("title", title);
        model.addAttribute("comment", comment);
        return "comments"; // La página comments.html se va a cargar con los nuevos valores
    }
}
