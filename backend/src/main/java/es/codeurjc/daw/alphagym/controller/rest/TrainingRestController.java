package es.codeurjc.daw.alphagym.controller.rest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.io.IOException;
import java.util.NoSuchElementException;

import es.codeurjc.daw.alphagym.dto.TrainingDTO;
import es.codeurjc.daw.alphagym.dto.UniqueTrainingDTO;
import es.codeurjc.daw.alphagym.dto.UserDTO;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.service.TrainingService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/trainings")
public class TrainingRestController {

    @Autowired
    TrainingService trainingService;

    @GetMapping("/")
    public Collection<TrainingDTO> getTrainings() {
        return trainingService.getAllDtoTrainings();
    }

    @GetMapping("/{trainingId}")
    public UniqueTrainingDTO getTrainingByIdd(@PathVariable Long trainingId) {
        return trainingService.getDtoTraining(trainingId);
    }

    @PostMapping("/")
    public ResponseEntity<TrainingDTO> createUser(@RequestBody TrainingDTO trainingDTO) throws SQLException, IOException {

        Training training = trainingService.toDomain(trainingDTO);
        trainingService.createTraining(training, null);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(training.getId()).toUri();

        return ResponseEntity.created(location).body(trainingService.toDTO(training));

    }

    @PutMapping("/{trainingId}")
    public TrainingDTO replacePost(@PathVariable long trainingId, @RequestBody TrainingDTO trainingDTO) throws SQLException {
        return trainingService.replaceTraining(trainingId, trainingDTO);
    }

    @DeleteMapping("/{trainingId}")
    public TrainingDTO deleteTraining(@PathVariable long trainingId) {
        return trainingService.deleteTraining(trainingId);
    }

    @GetMapping("/{trainingId}/image")
    public ResponseEntity<Object> getTrainingImage(@PathVariable long trainingId)   throws SQLException {
        Resource trainingImage = trainingService.getTrainingImage(trainingId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(trainingImage);
    }

    @PostMapping("/{trainingId}/image")
    public ResponseEntity<Object> createTrainingImage(  @PathVariable long trainingId, @RequestParam MultipartFile imageFile) throws IOException {
        URI location = fromCurrentRequest().build().toUri();
        trainingService.createTrainingImage(trainingId, location, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{trainingId}/image")
    public ResponseEntity<Object> replaceTrainingImage(  @PathVariable long trainingId, @RequestParam MultipartFile imageFile) throws IOException {
        trainingService.replaceTrainingImage(trainingId, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{trainingId}/image") public ResponseEntity<Object> deletePostImage(@PathVariable long trainingId) throws IOException, SQLException {
        trainingService.deleteTrainingImage(trainingId);
        return ResponseEntity.noContent().build();
    }

    /*@GetMapping("/")
    public ResponseEntity<List<TrainingDTO>> getPaginatedTrainings(
            @RequestParam Long trainingId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        List<TrainingDTO> trainingDTOs = trainingService.getPaginatedTrainingsDTO(trainingId, page, limit);
        return ResponseEntity.ok(trainingDTOs);
    }*/
}