package es.codeurjc.daw.alphagym.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String image;
    private String goal;
    private int calories;

    @OneToMany(cascade=CascadeType.ALL)
    private List<NutritionComment> comments;

    public Nutrition() {
    }

    public Nutrition(String name, String description, String image, String goal, int calories) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.goal = goal;
        this.calories = calories;
    }

    // Getters

    public Long getId() {
        return this.id;
    }

    public String getGoal() {
        return this.goal;
    }

    public int getCalories() {
        return this.calories;
    }

    public List<NutritionComment> getComments() {
        return comments;
    }

    public String getName() { 
        return name; }


    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setComments(List<NutritionComment> comments) {
        this.comments = comments;
    }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
}
