package es.codeurjc.helloword_vscode;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;

public class GreetingController {
    
    @GetMapping("/greeting")
    public String greeting(Model model) {

        model.addAttribute("name", "World");
        return "greeting_template";
    }
    
}
