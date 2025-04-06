package es.codeurjc.daw.alphagym.dto;

public record TrainingCommentDTO(
    Long id, 
    String name, 
    String description, 
    Boolean isNotified,
    TrainingDTO training,
    UserDTO user) {
}
