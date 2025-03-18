package es.codeurjc.daw.alphagym.controller.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.daw.alphagym.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


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

}
