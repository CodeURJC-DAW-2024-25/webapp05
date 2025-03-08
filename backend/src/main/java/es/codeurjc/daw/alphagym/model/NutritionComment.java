package es.codeurjc.daw.alphagym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class NutritionComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private String author;
    private String name;
    private boolean isNotified = false;

    @ManyToOne
    private Nutrition nutrition;

    protected NutritionComment() {
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

    public Nutrition getNutrition() {
        return nutrition;
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
    
    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }
}
