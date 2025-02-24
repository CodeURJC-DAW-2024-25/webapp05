package es.codeurjc.daw.alphagym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TrainingComment{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String comment;
    private String author;
    private String title;
    private boolean isNotified = false;

    protected TrainingComment() {
    }

    public TrainingComment(String comment, String author, String title) {
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

    public boolean getIsNotified() {
        return isNotified;
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

    public void setNotified(boolean isNotified) {
        this.isNotified = isNotified;
    }
}