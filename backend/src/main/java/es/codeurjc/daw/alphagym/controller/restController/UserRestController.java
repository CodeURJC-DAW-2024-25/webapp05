package es.codeurjc.daw.alphagym.controller.restController;

import java.security.Principal;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
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


@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    /*@Operation (Summary = "Gets the logged user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User found",
            content ={@Content(
                schema = @Schema(implementation = User.class)
            )}
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content
        ),
    }) */
    @RequestMapping("/me")
    public UserDTO me(HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		if(principal != null) {
			return userService.getUser(principal.getName());
		} else {
			throw new NoSuchElementException();
		}
	}

}
