package es.codeurjc.daw.alphagym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private long id;
    private String title;
    private String description; 
    private String image;

    public Training() {
    }

    public Training(String description, String image, String title) {
        this.description = description;
        this.image = image;
        this.title = title;
    }

    // Getters
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
