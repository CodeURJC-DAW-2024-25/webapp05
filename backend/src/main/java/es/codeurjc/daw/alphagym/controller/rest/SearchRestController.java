package es.codeurjc.daw.alphagym.controller.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import es.codeurjc.daw.alphagym.model.Nutrition;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.service.NutritionService;
import es.codeurjc.daw.alphagym.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/search")
public class SearchRestController {

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private NutritionService nutritionService;

    @Operation(summary = "Get training by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Training.class)) }),
            @ApiResponse(responseCode = "404", description = "Training not found",
                    content = @Content) })
    @GetMapping("/trainings/name")
    public ResponseEntity<?> getTrainingByName(@RequestParam String name) {
        List<Training> trainings = trainingService.findByName(name);

        if (trainings.isEmpty()) {
            return ResponseEntity.status(404).body("No trainings found with that name");
        } else {
            return ResponseEntity.ok(trainings);
        }
    }
    
    @Operation(summary = "Get nutrition by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nutrition found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Nutrition.class)) }),
            @ApiResponse(responseCode = "404", description = "Nutrition not found",
                    content = @Content) })
    @GetMapping("/nutritions/name")
    public ResponseEntity<?> getNutritionByName(@RequestParam String name) {
        List<Nutrition> nutritions = nutritionService.findByName(name);

        if (nutritions.isEmpty()) {
            return ResponseEntity.status(404).body("No nutritions found with that name");
        } else {
            return ResponseEntity.ok(nutritions);
        }
    }
}
