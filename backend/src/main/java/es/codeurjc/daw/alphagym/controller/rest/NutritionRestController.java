package es.codeurjc.daw.alphagym.controller.rest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.daw.alphagym.dto.NutritionDTO;
import es.codeurjc.daw.alphagym.service.NutritionService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/nutritions")
public class NutritionRestController {

    @Autowired
    private NutritionService nutritionService;

    @GetMapping("/")
    public Collection <NutritionDTO> getNutritions () {
        
        return nutritionService.getAllNutritionsDTO(); 
    }
    
    @GetMapping("/{id}")
    public NutritionDTO getNutrition(@PathVariable Long id) {

        return nutritionService.getNutritionDTO(id);
    }

   @PostMapping("/")
    public ResponseEntity<NutritionDTO> createNutrition (@RequestBody NutritionDTO nutritionDTO){

            nutritionDTO = nutritionService.createNutritionDTO(nutritionDTO);

            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(nutritionDTO.id()).toUri();

            return ResponseEntity.created(location).body(nutritionDTO);
    }

    @PutMapping("/{id}")
    public NutritionDTO editDiet (@PathVariable Long id, @RequestBody NutritionDTO updateNutritionDTO) throws SQLException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (nutritionService.isOwner(id, authentication) || authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            return nutritionService.editDietDTO(id, updateNutritionDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para editar este entrenamiento");
        }
    }

    @DeleteMapping("/{id}")
    public NutritionDTO deleteNutrition(@PathVariable Long id){

            return nutritionService.deleteDietDTO(id);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createNutritionImage(@PathVariable long id, @RequestParam MultipartFile imgNutrition) throws IOException {

        URI location = fromCurrentRequest().build().toUri();
        nutritionService.createNutritionImage(id, imgNutrition.getInputStream(), imgNutrition.getSize());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getNutritionImage(@PathVariable long id) throws SQLException, IOException {

        Resource postImage = nutritionService.getNutritionImage(id);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(postImage);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceNutritionImage(@PathVariable long id, @RequestParam MultipartFile imgNutrition) throws IOException {

        nutritionService.replaceNutritionImage(id, imgNutrition.getInputStream(), imgNutrition.getSize());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteNutritionImage(@PathVariable long id) throws IOException {

        nutritionService.deleteNutritionImage(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paginated")
    public ResponseEntity<List<NutritionDTO>> getPaginatedNutritions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        List<NutritionDTO> nutritionDTOs = nutritionService.getPaginatedNutritionsDTO(page, limit);
        return ResponseEntity.ok(nutritionDTOs);
    }


    
}
