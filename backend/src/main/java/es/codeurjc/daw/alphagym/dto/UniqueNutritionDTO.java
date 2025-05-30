package es.codeurjc.daw.alphagym.dto;

public record UniqueNutritionDTO(
    Long id,
    String name,
    String description,
    String goal,
    Integer calories,
    long userId) {
}
