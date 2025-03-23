package es.codeurjc.daw.alphagym.controller.rest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.daw.alphagym.dto.TrainingCommentDTO;
import es.codeurjc.daw.alphagym.dto.TrainingDTO;
import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.service.TrainingCommentService;
import es.codeurjc.daw.alphagym.service.TrainingService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/trainingComments")
public class TrainingCommentRestcontroller {

    @Autowired
    TrainingCommentService trainingCommentService;

    @Autowired
    TrainingService trainingService;

    @GetMapping("/all")
    public Collection<TrainingCommentDTO> getAllTrainingComments() {
        return trainingCommentService.getAllTrainingCommentsDTO();
    }
    
    @GetMapping("/{trainingId}/")
    public Collection<TrainingCommentDTO> getTrainingCommentsByTrainingId(@PathVariable Long trainingId) {
        return trainingCommentService.getTrainingCommentsByIdDTO(trainingId);
    }
    
    @GetMapping("/")
    public ResponseEntity<List<TrainingCommentDTO>> getPaginatedTrainingComments(
            @RequestParam Long trainingId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        List<TrainingCommentDTO> comments = trainingCommentService.getPaginatedCommentsDTO(trainingId, page, limit);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/")
    public ResponseEntity<TrainingCommentDTO> createTrainingComment(
            @RequestBody TrainingCommentDTO trainingCommentDTO) throws SQLException, IOException {

        trainingCommentDTO = trainingCommentService.createTrainingCommentDTO(trainingCommentDTO);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(trainingCommentDTO.id()).toUri();

        return ResponseEntity.created(location).body(trainingCommentDTO);

    }

    @DeleteMapping("/")
    public TrainingCommentDTO deleteTrainingComment(@RequestParam Long id) {
        return trainingCommentService.deleteCommentbyIdDTO(id);
    }

}
