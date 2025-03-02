package es.codeurjc.daw.alphagym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class NutritionComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private String author;
    private String name;
    private boolean isNotified = false;
    private Long nutritionId;


    protected NutritionComment() {
    }

    public NutritionComment(String description, String name, Long nutritionId) {
        this.description = description;
        this.name = name;
        this.nutritionId = nutritionId;
    }

    public NutritionComment(String description, String name) {
        this.description = description;     
        this.name = name;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public boolean getIsNotified() {
        return isNotified;
    }

    public Long getNutritionId() {
        return nutritionId;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name){
        this.name = name;
    } 

    public void setNotified(boolean isNotified) {
        this.isNotified = isNotified;
    }
    
    public void setNutritionId(Long nutritionId) {
        this.nutritionId = nutritionId;
    }
}
