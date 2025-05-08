package es.codeurjc.daw.alphagym.controller.rest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.io.IOException;
import es.codeurjc.daw.alphagym.dto.TrainingDTO;
import es.codeurjc.daw.alphagym.dto.UniqueTrainingDTO;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.service.TrainingService;
import es.codeurjc.daw.alphagym.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/trainings")
public class TrainingRestController {

    @Autowired
    TrainingService trainingService;

    @Autowired
    UserService userService;

    @Operation(summary = "Get all trainings")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all trainings", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
        @ApiResponse(responseCode = "404", description = "Trainings not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/")
    public Collection<TrainingDTO> getTrainings() {
        return trainingService.getAllDtoTrainings();
    }

    @Operation(summary = "Get a training by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UniqueTrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{trainingId}")
    public UniqueTrainingDTO getTrainingByIdd(@PathVariable long trainingId) {
        return trainingService.getDtoTraining(trainingId);
    }

    @Operation(summary = "Create a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<TrainingDTO> createTraining(@RequestBody TrainingDTO trainingDTO)
            throws SQLException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authentication != null ? trainingService.getAuthenticationUser() : null;

        Training training = trainingService.toDomain(trainingDTO);
        if(authentication!=null && !authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))){
            training = trainingService.createTraining(training, trainingService.getAuthenticationUser());

            if(user != null) {
                training.setUser(user);
            }
        }else{
            training = trainingService.createTraining(training, null);
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(training.getId()).toUri();

        return ResponseEntity.created(location).body(trainingService.toDTO(training));

    }

    @Operation(summary = "Replace a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training replaced", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{trainingId}")
    public TrainingDTO replacePost(@PathVariable long trainingId, @RequestBody TrainingDTO trainingDTO)
            throws SQLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (trainingService.isOwner(trainingId, authentication) || authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            return trainingService.replaceTraining(trainingId, trainingDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para editar este entrenamiento");
        }
    }

    @Operation(summary = "Replace parcial training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training atributes replaced", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PatchMapping("/{trainingId}")
    public TrainingDTO replaceParcialTraining(@PathVariable long trainingId, @RequestBody TrainingDTO trainingDTO)
            throws SQLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (trainingService.isOwner(trainingId, authentication) || authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            return trainingService.replaceParcialTraining(trainingId, trainingDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para editar este entrenamiento");
        }
    }

    @Operation(summary = "Delete a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{trainingId}")
    public TrainingDTO deleteTraining(@PathVariable long trainingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication!=null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return trainingService.deleteTraining(trainingId);
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para eliminar este entrenamiento");
        }
    }

    @Operation(summary = "Retrieve training image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training image found", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training image not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{trainingId}/image")
    public ResponseEntity<Object> getTrainingImage(@PathVariable long trainingId) throws SQLException {
        Resource trainingImage = trainingService.getTrainingImage(trainingId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(trainingImage);
    }

    @Operation(summary = "Create training image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training image created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{trainingId}/image")
    public ResponseEntity<Object> createTrainingImage(@PathVariable long trainingId,
            @RequestParam MultipartFile imageFile) throws IOException {
        URI location = fromCurrentRequest().build().toUri();
        trainingService.createTrainingImage(trainingId, location, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Replace training image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Training image replaced", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{trainingId}/image")
    public ResponseEntity<Object> replaceTrainingImage(@PathVariable long trainingId,
            @RequestParam MultipartFile imageFile) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (trainingService.isOwner(trainingId, authentication) || authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            trainingService.replaceTrainingImage(trainingId, imageFile.getInputStream(), imageFile.getSize());
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para editar este entrenamiento");
        }

    }

    @Operation(summary = "Delete training image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Training image deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training image not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{trainingId}/image")
    public ResponseEntity<Object> deletePostImage(@PathVariable long trainingId) throws IOException, SQLException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication!=null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
             trainingService.deleteTrainingImage(trainingId);
             return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para editar este entrenamiento");
        }

    }

    @Operation(summary = "Get paginated trainings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/paginated")
    public ResponseEntity<List<TrainingDTO>> getPaginatedTrainings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        List<TrainingDTO> trainingDTOs = trainingService.getPaginatedTrainingsDTO(page, limit);
        return ResponseEntity.ok(trainingDTOs);
    }

    @Operation(summary = "Subscribe a user to a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User subscribed to training successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training or User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @PostMapping("/subscribed/{trainingId}")
    public ResponseEntity<String> subscribeTraining(@PathVariable Long trainingId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            boolean alreadySubscribed = trainingService.subscribeTrainingDTO(trainingId, email);
            
            if (alreadySubscribed) {
                return ResponseEntity.ok("The user was already subscribed to this training.");
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body("User subscribed from training successfully");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @Operation(summary = "Unsubscribe a user from a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User unsubscribed from training successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training or User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @PostMapping("/unsubscribed/{trainingId}")
    public ResponseEntity<String> unsubscribeTraining(@PathVariable Long trainingId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            boolean unsubscribed = trainingService.unsubscribeTrainingDTO(trainingId, email);
    
            if (unsubscribed) {
                return ResponseEntity.ok("User unsubscribed from training successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User is not subscribed to this training. Subscribe first before unsubscribing.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/isSubscribed/{trainingId}")
    public ResponseEntity<Boolean> isSubscribed(@PathVariable long trainingId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Training training = trainingService.getTraining(trainingId);
            boolean subscribed = userService.isSubscribedToTraining(authentication.getName(), training);
            return ResponseEntity.ok(subscribed);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

    }
}