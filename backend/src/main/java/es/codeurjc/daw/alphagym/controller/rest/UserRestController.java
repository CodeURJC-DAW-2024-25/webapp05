package es.codeurjc.daw.alphagym.controller.rest;

import java.io.IOException;
import java.net.URI;

import es.codeurjc.daw.alphagym.dto.NutritionCommentDTO;
import es.codeurjc.daw.alphagym.dto.NutritionDTO;
import es.codeurjc.daw.alphagym.dto.TrainingCommentDTO;
import es.codeurjc.daw.alphagym.dto.TrainingDTO;
import es.codeurjc.daw.alphagym.service.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import es.codeurjc.daw.alphagym.dto.UserDTO;
import es.codeurjc.daw.alphagym.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

        @Autowired
        private UserService userService;

        @Autowired
        private NutritionService nutritionService;

        @Autowired
        private TrainingService trainingService;

        @Autowired
        private NutritionCommentService nutritionCommentService;

        @Autowired
        private TrainingCommentService trainingCommentService;

        @Operation(summary = "Get all users")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found all users", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
                        @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Users not found", content = @Content)
        })
        @GetMapping("/all")
        public Collection<UserDTO> getUsers() {

                return userService.getUsers();

        }

        @Operation(summary = "Get authenticated user", description = "Retrieve the currently authenticated user.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Authenticated user found"),
                        @ApiResponse(responseCode = "401", description = "User not authenticated")
        })
        @GetMapping("/me")
        public ResponseEntity<UserDTO> getAuthenticatedUser() {
                return userService.getAuthenticatedUserDto()
                                .map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.status(401).build());
        }

        @Operation(summary = "Get a user by its id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found the user", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                        @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
        })
        @GetMapping("/{id}")
        public UserDTO getUser(@Parameter(description = "User id", required = true) @PathVariable Long id) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication == null || !authentication.isAuthenticated()) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
                }

                boolean isAdmin = authentication.getAuthorities().stream()
                                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

                if (!isAdmin) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
                }

                Optional<User> user = userService.findById(id);

                if (user.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
                }

                return userService.getUser(user.get().getName());
        }

        @Operation(summary = "Gets the image of a user by its id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found the user image", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                        @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content),
                        @ApiResponse(responseCode = "404", description = "User not found, user image not found or doesn't have permission to access it", content = @Content),
        })
        @GetMapping("/{id}/image")
        public ResponseEntity<Object> getUserImage(@PathVariable long id) throws SQLException {

                Resource userImage;
                try {
                        userImage = userService.getUserImage(id);
                        if (userImage == null) {
                                throw new NoSuchElementException("User image not found for id: " + id);
                        }
                } catch (NoSuchElementException e) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User image not found");
                }

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDTO authenticatedUser = userService.getAuthenticatedUserDto()
                                .orElseThrow(() -> new NoSuchElementException("User not authenticated"));

                if (authentication.getAuthorities().stream()
                                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
                                || (authenticatedUser.id().equals(id))) {
                        return ResponseEntity.ok()
                                        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                                        .body(userImage);
                }

                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                "Access denied, you are not allowed to access this user image");
        }

        @Operation(summary = "Registers a new user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User registered correctly", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Bad request, maybe one of the user attributes is missing or the type is not valid", content = @Content),
                        @ApiResponse(responseCode = "409", description = "User already exists", content = @Content)
        })
        @PostMapping("/new")
        public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
                if (userService.existsByEmail(userDTO.email())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                        .body(Map.of("error", "Email is already in use"));
                }

                userDTO = userService.createUser(userDTO);

                URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(userDTO.id())
                                .toUri();

                return ResponseEntity.created(location).body(userDTO);
        }

        @Operation(summary = "Deletes a user by its id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User deleted correctly", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
                        @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteUser(@PathVariable Long id) {
                try {
                        userService.deleteUser(id);
                        return ResponseEntity.ok("User deleted correctly");
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Could not delete the user due to an internal error. " +
                                                        "It is possible that the user with the specified ID does not exist.");
                }
        }

        @Operation(summary = "Updates the image of a user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Image created correctly", content = @Content),
                        @ApiResponse(responseCode = "204", description = "Image updated correctly", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
                        @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content),
                        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
        })
        @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
        @PreAuthorize("isAuthenticated()")
        @PutMapping("/{id}/image")
        public ResponseEntity<Object> replaceUserImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
                        throws IOException {

                // An user, can edit only his own user image
                Optional<UserDTO> authenticatedUserDto = userService.getAuthenticatedUserDto();
                if (authenticatedUserDto.isEmpty() || (!authenticatedUserDto.get().id().equals(id)
                                && !authenticatedUserDto.get().roles().contains("ROLE_ADMIN"))) {
                        throw new AccessDeniedException("You are not allowed to edit this user.");
                }

                userService.replaceUserImage(id, imageFile.getInputStream(), imageFile.getSize());

                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Update a user by its id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User updated correctly", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
                        @ApiResponse(responseCode = "400", description = "User not updated", content = @Content),
                        @ApiResponse(responseCode = "403", description = "User not authorized", content = @Content)
        })
        @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
        @PreAuthorize("isAuthenticated()")
        @PutMapping("/{id}")
        public UserDTO replaceUser(@RequestBody UserDTO userDTO, @PathVariable Long id) throws SQLException {

                UserDTO authenticatedUser = userService.getAuthenticatedUserDto()
                                .orElseThrow(() -> new NoSuchElementException("User not authenticated"));

                // if is admin, can edit any user
                if (authenticatedUser.roles().contains("ROLE_ADMIN")) {
                        return userService.updateUser(id, userDTO);
                }

                // if is user, can edit only his own user
                if (authenticatedUser.id().equals(id) && userDTO.email() != null && !userDTO.email().trim().isEmpty()) {
                        return userService.updateUser(id, userDTO);
                }

                // If not achieve any condition, return error 403 (Forbidden)
                throw new AccessDeniedException("You are not allowed to edit this user.");
        }

        

        @Operation(summary = "Delete user image")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User image deleted", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden: You are not allowed to delete this user image", content = @Content),
                        @ApiResponse(responseCode = "404", description = "User image not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @DeleteMapping("/{id}/image")
        public ResponseEntity<String> deleteUserImage(@PathVariable long id) {
                try {
                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                        if (authentication != null && authentication.getAuthorities().stream()
                                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                                                        .equals("ROLE_ADMIN"))) {

                                userService.deleteUserImage(id);
                                return ResponseEntity.ok("User image deleted successfully");
                        } else {
                                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                                .body("Forbidden: You are not allowed to delete this user image.");
                        }

                } catch (NoSuchElementException e) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body("User image not found or user does not exist.");
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("An internal error occurred while trying to delete the user image.");
                }
        }

        @GetMapping("/reportedComments")
        public ResponseEntity<List<String>> getReportedComments() {
                List<String> reportedComments = new ArrayList<>();

                Long[] reportsArray1 = trainingCommentService.getReportAmmmounts();
                Long[] reportsArray2 = nutritionCommentService.getReportAmmmounts();

                if (reportsArray1.length == 0 && reportsArray2.length == 0) {
                        return ResponseEntity.noContent().build(); // Return 204 No Content if there are no reported
                                                                   // comments
                } else {
                        reportedComments.add(
                                        "Total comments: " + (reportsArray1[0] + reportsArray2[0] + reportsArray1[1]
                                                        + reportsArray2[1]));
                        reportedComments.add("Total reported comments: " + (reportsArray1[0] + reportsArray2[0]));
                        reportedComments.add("Reported training comments: " + reportsArray1[0]);
                        reportedComments.add("Reported nutrition comments: " + reportsArray2[0]);
                        reportedComments.add("Total valid comments: " + (reportsArray1[1] + reportsArray2[1]));
                        reportedComments.add("Valid training comments: " + reportsArray1[1]);
                        reportedComments.add("Valid nutrition comments: " + reportsArray2[1]);
                }

                return ResponseEntity.ok(reportedComments); // Return 200 OK with the list of comments
        }

        @GetMapping("/reportedCommentsValues")
        public ResponseEntity<List<Long>> getReportedCommentsValues() {
                List<Long> reportedComments = new ArrayList<>();

                Long[] reportsArray1 = trainingCommentService.getReportAmmmounts();
                Long[] reportsArray2 = nutritionCommentService.getReportAmmmounts();

                if (reportsArray1.length == 0 && reportsArray2.length == 0) {
                        return ResponseEntity.noContent().build(); // Return 204 No Content if there are no reported
                                                                   // comments
                } else {
                        reportedComments.add(
                                        (reportsArray1[0] + reportsArray2[0] + reportsArray1[1] + reportsArray2[1]));
                        reportedComments.add((reportsArray1[0] + reportsArray2[0]));
                        reportedComments.add(reportsArray1[0]);
                        reportedComments.add(reportsArray2[0]);
                        reportedComments.add((reportsArray1[1] + reportsArray2[1]));
                        reportedComments.add(reportsArray1[1]);
                        reportedComments.add(reportsArray2[1]);
                }

                return ResponseEntity.ok(reportedComments); // Return 200 OK with the list of comments
        }

        @GetMapping("/reportedTrainingComments")
        public ResponseEntity<List<TrainingCommentDTO>> getReportedTrainingComments() {
                List<TrainingCommentDTO> reportedComments = trainingCommentService.getReportedCommentsDTO();

                if (reportedComments.size() == 0) {
                        return ResponseEntity.noContent().build(); // Return 204 No Content if there are no reported comments
                }
                return ResponseEntity.ok(reportedComments); // Return 200 OK with the list of comments
        }

        @GetMapping("/reportedNutritionComments")
        public ResponseEntity<List<NutritionCommentDTO>> getReportedNutritionComments() {
                List<NutritionCommentDTO> reportedComments = nutritionCommentService.getReportedCommentsDTO();

                if (reportedComments.size() == 0) {
                        return ResponseEntity.noContent().build(); // Return 204 No Content if there are no reported comments
                }
                return ResponseEntity.ok(reportedComments); // Return 200 OK with the list of comments
        }

        @Operation(summary = "Get all trainings of user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found all trainings", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
                        @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Trainings not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PreAuthorize("isAuthenticated()")
        @GetMapping("/trainingList")
        public Collection<TrainingDTO> getUserTrainings() {
                return trainingService.getAllDtoUserTrainings();
        }

        @Operation(summary = "Get all nutritions of user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found all nutritions", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = NutritionDTO.class)) }),
                        @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
                        @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Nutritions not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PreAuthorize("isAuthenticated()")
        @GetMapping("/nutritionList")
        public Collection<NutritionDTO> getUserNutritions() {
                return nutritionService.getAllDtoUserNutritions();
        }

}
