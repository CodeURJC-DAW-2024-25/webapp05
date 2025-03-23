package es.codeurjc.daw.alphagym.dto;

public record TrainingCommentDTO(
    Long id, 
    String name, 
    String description, 
    boolean isNotified,
    boolean canEdit, 
    TrainingDTO training,
    UserDTO user) {
}
