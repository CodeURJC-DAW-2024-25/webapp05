package es.codeurjc.daw.alphagym.model;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private long id;
    private String name;
    private String title; 
    private String description;
    private String image;

    public Nutrition() {
    }

    public Nutrition(String name, String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    // Getters
    public String getName() { return name; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    // Setters
    public void setName(String name) { this.name = name; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
}
