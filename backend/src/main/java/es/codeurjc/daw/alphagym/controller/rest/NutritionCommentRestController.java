package es.codeurjc.daw.alphagym.controller.rest;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.daw.alphagym.dto.NutritionCommentDTO;
import es.codeurjc.daw.alphagym.dto.NutritionDTO;
import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.NutritionComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.service.NutritionCommentService;
import es.codeurjc.daw.alphagym.service.NutritionService;
import es.codeurjc.daw.alphagym.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/nutritionComments") // server adress where the controller is listening
public class NutritionCommentRestController {
    @Autowired
    private NutritionCommentService nutritionCommentService;

    @Autowired
    private NutritionService nutritionService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public Collection<NutritionCommentDTO> getNutritionComments() {
        return nutritionCommentService.getAllNutritionCommentsDTO();
    }

    @GetMapping("/{nutritionId}")
    public Collection<NutritionCommentDTO> getNutritionCommentById(@PathVariable Long nutritionId) {
        return nutritionCommentService.getNutritionCommentsDTO(nutritionId);
    }

    @GetMapping("/")
    public ResponseEntity<List<NutritionCommentDTO>> getPaginatedNutritionComments(
            @RequestParam Long nutritionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        List<NutritionCommentDTO> comments = nutritionCommentService.getPaginatedCommentsDTO(nutritionId, page, limit);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/")
    public ResponseEntity<NutritionCommentDTO> createNutritionComment(
            @RequestBody NutritionCommentDTO nutritionCommentDTO) {

        // Guardar el comentario y obtener el DTO resultante
        nutritionCommentDTO = nutritionCommentService.createNutritionComment(nutritionCommentDTO);

        // Construir la URI para el recurso creado
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nutritionCommentDTO.id()) // Suponiendo que NutritionCommentDTO tiene un método id()
                .toUri();

        // Retornar la respuesta con código 201 Created y el DTO en el cuerpo
        return ResponseEntity.created(location).body(nutritionCommentDTO);
    }

    /* 
    @PutMapping("/{id}")
    public ResponseEntity<NutritionCommentDTO> updateNutritionComment(
            @PathVariable long id,
            @RequestBody NutritionCommentDTO updatedCommentDTO) {

        NutritionCommentDTO updatedComment = nutritionCommentService.replaceNutritionComment(id, updatedCommentDTO);
        return ResponseEntity.ok(updatedComment);
    }
    */

    /*
     * @PostMapping("/")
     * public ResponseEntity<NutritionCommentDTO> createNutritionComment(
     * 
     * @RequestBody NutritionCommentDTO nutritionCommentDTO,
     * 
     * @RequestParam Long nutritionId) {
     * 
     * NutritionComment nutritionComment =
     * nutritionCommentService.toDomain(nutritionCommentDTO);
     * Nutrition nutrition = nutritionService.getNutrition(nutritionId);
     * NutritionDTO nutritionDTO = nutritionService.toDTO(nutrition);
     * nutritionComment.setNutrition(nutrition);
     * // nutritionCommentService.createNutritionComment(nutritionComment,null);
     * URI location = ServletUriComponentsBuilder.fromCurrentRequest()
     * .path("/{id}")
     * .buildAndExpand(nutritionComment.getId()).toUri();
     * return ResponseEntity.created(location).body(nutritionCommentService.toDTO(
     * nutritionComment));
     * }
     */

    /*
     * @PostMapping("/")
     * public ResponseEntity<NutritionCommentDTO> createNutritionComment(
     * 
     * @RequestBody NutritionCommentDTO nutritionCommentDTO,
     * 
     * @RequestParam Long nutritionId) {
     * // Obtener los datos relacionados (esto depende de cómo gestiones los
     * objetos)
     * Nutrition nutrition = nutritionCommentService.toDomain(nutritionCommentDTO);
     * User user = userService.getUserById(nutritionCommentDTO.getId());
     * 
     * // Crear el comentario con DTO
     * NutritionCommentDTO createdComment =
     * nutritionCommentService.createNutritionCommentDTO(nutritionCommentDTO,
     * nutrition, user);
     * 
     * // Construir la URI del nuevo recurso
     * URI location = ServletUriComponentsBuilder.fromCurrentRequest()
     * .path("/{id}")
     * .buildAndExpand(createdComment.getId())
     * .toUri();
     * 
     * return ResponseEntity.created(location).body(createdComment);
     * }
     */

}
