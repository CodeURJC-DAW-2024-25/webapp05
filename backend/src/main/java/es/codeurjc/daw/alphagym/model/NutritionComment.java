package es.codeurjc.daw.alphagym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class NutritionComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private long id;
    private String comment;
    private String author;
    private String title;

    // Constructor necesario para la carga desde BBDD
    protected NutritionComment() {
    }

    public NutritionComment(String comment, String author, String title) {
        this.comment = comment;
        this.author = author;
        this.title = title;
    }

    // Getters

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public String getAuthor() {
        return author;
    }

    // Setters
    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title){
        this.title = title;
    } 

    
}
