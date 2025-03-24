package es.codeurjc.daw.alphagym.controller.rest;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.daw.alphagym.dto.NutritionCommentDTO;
import es.codeurjc.daw.alphagym.service.NutritionCommentService;
import org.springframework.web.bind.annotation.PutMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/nutritionComments") // server adress where the controller is listening
public class NutritionCommentRestController {
    @Autowired
    private NutritionCommentService nutritionCommentService;

    @Operation(summary = "Get all nutrition comments",description="Get all nutrition comments")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Nutrition comments found"),
        @ApiResponse(responseCode="400",description="Bad request"),
        @ApiResponse(responseCode="404",description="Nutrition comments not found")
    })
    @GetMapping("/all")
    public Collection<NutritionCommentDTO> getNutritionComments() {
        return nutritionCommentService.getAllNutritionCommentsDTO();
    }

    @Operation(summary = "Get nutrition comments by nutrition id",description="Get nutrition comments by nutrition id")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Nutrition comments found"),
        @ApiResponse(responseCode="400",description="Bad request"),
        @ApiResponse(responseCode="404",description="Nutrition comments not found")
    })
    @GetMapping("/{nutritionId}")
    public Collection<NutritionCommentDTO> getNutritionCommentById(@PathVariable Long nutritionId) {
        return nutritionCommentService.getNutritionCommentsDTO(nutritionId);
    }
    @Operation(summary = "Get paginated nutrition comments",description="Get paginated nutrition comments")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Nutrition comments found"),
        @ApiResponse(responseCode="400",description="Bad request"),
        @ApiResponse(responseCode="404",description="Nutrition comments not found")
    })
    @GetMapping("/")
    public ResponseEntity<List<NutritionCommentDTO>> getPaginatedNutritionComments(
            @RequestParam Long nutritionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        List<NutritionCommentDTO> comments = nutritionCommentService.getPaginatedCommentsDTO(nutritionId, page, limit);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Create nutrition comment",description="Create nutrition comment")
    @ApiResponses(value={
        @ApiResponse(responseCode="201",description="Nutrition comment created"),
        @ApiResponse(responseCode="400",description="Bad request"),
        @ApiResponse(responseCode="403",description="Forbidden-Access denied"),
        @ApiResponse(responseCode="404",description="Nutrition comment not found")
    })
    @PostMapping("/")
    public ResponseEntity<NutritionCommentDTO> createNutritionComment(
            @RequestBody NutritionCommentDTO nutritionCommentDTO) {

        // Save the comment and get DTO
        nutritionCommentDTO = nutritionCommentService.createNutritionCommentDTO(nutritionCommentDTO);

        // Build the URI for the created resource
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nutritionCommentDTO.id())
                .toUri();

        // Return the response with 201 Created code and the DTO in the body
        return ResponseEntity.created(location).body(nutritionCommentDTO);
    }

    @Operation(summary = "Update nutrition comment",description="Update nutrition comment")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Nutrition comment updated"),
        @ApiResponse(responseCode="400",description="Bad request"),
        @ApiResponse(responseCode="403",description="Forbidden-Access denied"),
        @ApiResponse(responseCode="404",description="Nutrition comment not found")
    })
    @PutMapping("/{id}")
    public NutritionCommentDTO updateNutritionComment(
            @RequestParam Long id,
            @RequestBody NutritionCommentDTO updatedCommentDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (nutritionCommentService.isOwnerComment(id, authentication) || authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return nutritionCommentService.replaceNutritionCommentDTO(id, updatedCommentDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para editar este entrenamiento");
        }

    }
    @Operation(summary = "Delete nutrition comment",description="Delete nutrition comment")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Nutrition comment deleted"),
        @ApiResponse(responseCode="400",description="Bad request"),
        @ApiResponse(responseCode="403",description="Forbidden-Access denied"),
        @ApiResponse(responseCode="404",description="Nutrition comment not found")
    })
    @DeleteMapping("/")
    public NutritionCommentDTO deleteNutritionComment(@RequestParam Long id) {
        return nutritionCommentService.deleteCommentbyIdDTO(id);
    }

    @Operation(summary = "Report nutrition comment",description="Report nutrition comment")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Nutrition comment reported"),
        @ApiResponse(responseCode="400",description="Bad request"),
        @ApiResponse(responseCode="403",description="Forbidden-Access denied"),
        @ApiResponse(responseCode="404",description="Nutrition comment not found")
    })
    @PutMapping("/report")
    public ResponseEntity<NutritionCommentDTO> reportComment(@RequestParam Long commentId) {
        NutritionCommentDTO updatedComment = nutritionCommentService.reportNutritionComment(commentId);
        return ResponseEntity.ok(updatedComment);
    }

    @Operation(summary = "Unreport nutrition comment",description="Unreport nutrition comment")
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Nutrition comment unreported"),
        @ApiResponse(responseCode="400",description="Bad request"),
        @ApiResponse(responseCode="403",description="Forbidden-Access denied"),
        @ApiResponse(responseCode="404",description="Nutrition comment not found")
    })
    @PutMapping("/valid")
    public ResponseEntity<NutritionCommentDTO> unreportComment(@RequestParam Long commentId) {
        NutritionCommentDTO updatedComment = nutritionCommentService.unreportNutritionComment(commentId);
        return ResponseEntity.ok(updatedComment);
    }

}
