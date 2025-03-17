package es.codeurjc.daw.alphagym.dto;

public record NutritionDTO(
    Long id,
    String name,
    String description,
    boolean image,
    String goal,
    int calories) {
}
