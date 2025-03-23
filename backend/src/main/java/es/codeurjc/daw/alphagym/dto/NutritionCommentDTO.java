package es.codeurjc.daw.alphagym.dto;

public record NutritionCommentDTO(
    Long id,
    String description,
    String name,
    boolean isNotified,
    NutritionDTO nutrition,//Check if this is correct
    UserDTO user //Check if this is correct
) {    
    
}
