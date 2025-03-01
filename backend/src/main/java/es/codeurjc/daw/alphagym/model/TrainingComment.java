package es.codeurjc.daw.alphagym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class TrainingComment{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private String author;
    private String title;
    private boolean isNotified = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id")
    private Training training;

    public Training getTraining() { 
        return training; 
    }
    public void setTraining(Training training) {
        this.training = training;
    }

    protected TrainingComment() {
    }

    public TrainingComment(String description, String author, String title) {
        this.description = description;
        this.author = author;
        this.title = title;
    }

    public TrainingComment(String description, String title) {
        this.description = description;     
        this.title = title;
    }

    // Getters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getdescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public boolean getIsNotified() {
        return isNotified;
    }

    // Setters
    public void setdescription(String description) {
        this.description = description;
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