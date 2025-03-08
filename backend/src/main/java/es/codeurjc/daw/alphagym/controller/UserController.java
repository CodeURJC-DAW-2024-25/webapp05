package es.codeurjc.daw.alphagym.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;

import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @ModelAttribute("user")
    public void addAttributes(Model model, HttpServletRequest request){

        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            Optional <User> user = userService.findByEmail(principal.getName()); //se usa getName porque asi se hace desde security
            if (user.isPresent()){
                if (user.get().isRole("USER")){
                    model.addAttribute("user", true);
                }
                if (user.get().isRole("ADMIN")){
                    model.addAttribute("admin", true);
                }
            }
            model.addAttribute("logged", true);
            model.addAttribute("userName", principal.getName());
        } else {
            model.addAttribute("logged", false);
        }

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
    }

    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        return "index";
    }
    
    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return "register";
        } else {
            return "redirect:/index";
        }
    }
    
    @PostMapping("/user/new")
    public String createUser(Model model, @RequestParam MultipartFile image, @RequestParam String name,
            @RequestParam String email, @RequestParam String password) {
        try {
            // Check if user already exists with the given email
            Optional<User> existingUser = userService.findByEmail(email);
           
            if (existingUser.isPresent()) {
                // User exists, so we return an error message
                model.addAttribute("error", "El email ya está en uso");
                return "register"; 
            }

            // Manejar caso en que no se suba imagen
            if (image == null || image.isEmpty()) {
                userService.createUser(name, email, password, "USER"); 
            } else {
                userService.createUser(name, email, password, image, "USER"); 
            }

            return "redirect:/login"; 

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ha ocurrido un error.");
            return "register"; 
        }
    }
    
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return "login";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/account")
    public String profile(Model model, HttpServletRequest request) {

        String principal = request.getUserPrincipal().getName();
        Optional<User> user = userService.findByEmail(principal);

        if (user.isPresent()) {

            //aqui hay que preguntar al servicio si tiene nutricion y rutina y luego añadirle abajo con addAttribute
            
            model.addAttribute("user", user.get());
            model.addAttribute("trainings", user.get().getTrainings());
            //aqui añadir 
            return "account";

        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/editAccount")
    public String editAccount(Model model, @ModelAttribute User user, boolean removeImage, MultipartFile imageField)
        throws IOException, SQLException {

        try {
            Optional<User> userOld = userService.findById(user.getId());

            if (userOld.isPresent()) {
                User updateUser = userOld.get();

                // Actualizar nombre solo si no está vacío
                if (user.getName() != null && !user.getName().trim().isEmpty()) {
                    userService.updateUserName(user.getId(), user.getName());
                }

                // Actualizar email solo si no está vacío
                if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                    userService.updateUserEmail(user.getId(), user.getEmail());
                }

                // Actualizar imagen si se proporciona o eliminarla si es necesario
                userService.updateUserImage(updateUser, removeImage, imageField);

                // Guardar usuario con los cambios realizados
                userService.save(updateUser);

                return "redirect:/account"; 

            } else {
                model.addAttribute("error", "Usuario no encontrado");
                return "editAccount"; // Regresar al formulario de edición si no se encuentra el usuario
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ha ocurrido un error inesperado.");
            return "editAccount"; // Regresar con error si algo falla
        }
    }

    @GetMapping("/users/{id}/image")
	public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

		Optional<User> user = userService.findById(id);
		if (user.isPresent() && user.get().getImg_user() != null) {

			Resource file = new InputStreamResource(user.get().getImg_user().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(user.get().getImg_user().length()).body(file);

		} else {
			return ResponseEntity.notFound().build();
		}
	}
    
    @GetMapping("/admin")
    public String admin(Model model, HttpServletRequest request) {

        String principal = request.getUserPrincipal().getName();
        Optional<User> admin = userService.findByEmail(principal);

        if (admin.isPresent()) {

            //aqui hay meterle los comentarios reportados
            //model.addAtribute(isNotified);
            
            model.addAttribute("admin", admin.get());
            //aqui añadir 
            return "admin";

        } else {
            return "redirect:/login";
        }
    }
    
    
}