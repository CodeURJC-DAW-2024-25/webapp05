package es.codeurjc.daw.alphagym.controller.restController;

import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.security.Principal;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.daw.alphagym.dto.UserDTO;
import es.codeurjc.daw.alphagym.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserDTO me(HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		if(principal != null) {
			return userService.getUser(principal.getName());

		} else {
			throw new NoSuchElementException();
		}
	}

    @GetMapping("/{id}")
    public UserDTO getUser(@Parameter(description = "User id", required = true) @PathVariable Long id) {

        return getUser(id);

    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {

        userDTO = userService.createUser(userDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(userDTO.id()).toUri();

        return ResponseEntity.created(location).body(userDTO); 
        
    }
    
    @PutMapping("/{id}")
    public UserDTO replaceUser(@RequestBody UserDTO updateUserDTO, @PathVariable Long id) {

        return userService.replaceUser(id, updateUserDTO);

    } 

}
