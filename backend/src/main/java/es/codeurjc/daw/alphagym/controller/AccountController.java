package es.codeurjc.daw.alphagym.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.service.UserService;


@Controller
public class AccountController {

    /*@Autowired
    private UserService userService;

    @ModelAttribute("user")
    public void addAttributes(Model model, HttpServletRequest request){

        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            model.addAttribute("logged", true);
            model.addAttribute("userName", principal.getName());
            model.addAttribute("email", principal.getEmail());
            model.addAttribute("user", request.isUserInRole("USER"));
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        } else {
            model.addAttribute("logged", false);
        }
    }

    @GetMapping("/account")
    public String showAccount(Model model, HttpServletRequest request) {

        String principal = request.getUserPrincipal().getEmail();
        
        Optional<User> user = userService.findByEmail(email);

        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        } else {
            model.addAttribute("error", "User not found");
        }
        

        return "account";
    }
 */
}