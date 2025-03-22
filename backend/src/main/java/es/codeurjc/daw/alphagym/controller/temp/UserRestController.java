package es.codeurjc.daw.alphagym.controller.restController;

import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpHeaders;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.security.Principal;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.daw.alphagym.dto.UserDTO;
import es.codeurjc.daw.alphagym.model.User;
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

    @Operation (summary = "Gets the logged user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found the user",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=User.class)
                )}
        ),
        @ApiResponse(
            responseCode = "401",
            description = "User not authorized",
            content = @Content
        ),
    })
    @GetMapping("/me")
    public UserDTO me(HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		if(principal != null) {
			return userService.getUser(principal.getName());

		} else {
			throw new NoSuchElementException();
		}
	}

    @Operation(summary = "Get a user by its id") 
    @ApiResponses(value = { 
        @ApiResponse( 
            responseCode = "200", 
            description = "Found the user", 
            content = {@Content( 
                mediaType = "application/json", 
                schema = @Schema(implementation=User.class) 
                )} 
        ), 
        @ApiResponse( 
            responseCode = "400", 
            description = "Invalid id supplied", 
            content = @Content 
            ), 
        @ApiResponse(
            responseCode = "401",
            description = "User not authorized",
            content = @Content
        ),
        @ApiResponse( 
            responseCode = "404", 
            description = "User not found", 
            content = @Content 
        ) 
    })
    @GetMapping("/{id}")
    public UserDTO getUser(@Parameter(description = "User id", required = true) @PathVariable Long id) {

        return getUser(id);

    }

    @Operation (summary = "Gets the image of a user by its id")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found the user image",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid id supplied",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "User not authorized",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found, user image not found or doesn't have permission to access it",
            content = @Content
        ),
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getUserImage(@PathVariable long id) throws IOException, SQLException{

        InputStreamResource userImage = userService.getUserImage(id);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
            .body(userImage);
            
    }

    @Operation (summary = "Registers a new user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User registered correctly",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request, maybe one of the user attributes is missing or the type is not valid",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "User already exists",
            content = @Content
        )
    })
    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {

        userDTO = userService.createUser(userDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(userDTO.id()).toUri();

        return ResponseEntity.created(location).body(userDTO); 
        
    }

    @Operation (summary = "Registers the image of a user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Image created correctly",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "User not authorized",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "User not authorized",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        )
    })
    @PostMapping("{id}/image")
    public ResponseEntity<Object> createUserImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

        userService.createUserImage(id, location, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.created(location).build();
    }
    
    @Operation(summary = "Update a user by its id")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User updated correctly",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=User.class)
        )}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "User not updated",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "User not authorized",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public UserDTO replaceUser(@RequestBody UserDTO updatedUserDTO, @PathVariable Long id) throws SQLException {

        return userService.replaceUser(id, updatedUserDTO);

    } 

    @Operation (summary = "Updates the image of a user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Image created correctly",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Image updated correctly",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "User not authorized",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "User not authorized",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        )
    })
    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceUserImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

        userService.replaceUserImage(id, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.noContent().build();
    
    }

    
}
