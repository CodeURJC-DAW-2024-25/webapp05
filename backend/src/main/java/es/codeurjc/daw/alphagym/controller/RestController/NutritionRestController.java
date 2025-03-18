package es.codeurjc.daw.alphagym.controller.restController;
/*package es.codeurjc.daw.alphagym.controller.RestController;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import es.codeurjc.daw.alphagym.dto.NutritionDTO;
import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.service.NutritionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionRESTController {

    @Autowired
    private NutritionService nutritionService;

    @GetMapping("/")
    public Collection <NutritionDTO> getNutritions () {
        
        return nutritionService.getAllNutritions(); 
    }
    
    @GetMapping("/{id}")
    public NutritionDTO getNutrition(@PathVariable Long id) {

        return nutritionService.getNutrition(id);
    }

    @PostMapping("/")
    public ResponseEntity<NutritionDTO> createNutrition (@RequestBody NutritionDTO nutritionDTO){

            nutritionDTO = nutritionService.createNutrition(nutritionDTO);

            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(nutritionDTO.getId()).toUri();

            return ResponseEntity.created(location).body(nutritionDTO);
    }

    @PutMapping("/{id}")
    public NutritionDTO editDiet (@PathVariable Long id, @RequestBody NutritionDTO updateNutritionDTO) throws SQLException{

            return nutritionService.editDiet(id, updateNutritionDTO);
    }

    @DeleteMapping("/{id}")
    public NutritionDTO deleteNutrition(@PathVariable Long id){

            return nutritionService.deleteDiet(id);
    }
}*/
