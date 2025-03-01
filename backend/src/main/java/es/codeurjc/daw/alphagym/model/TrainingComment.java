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
    private String comment;
    private String author;
    private String name;
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

    public TrainingComment(String comment, String author, String name) {
        this.comment = comment;
        this.author = author;
        this.name = name;
    }

    public TrainingComment(String comment, String name) {
        this.comment = comment;     
        this.name = name;
    }

    // Getters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
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

    public void name(String name){
        this.name = name;
    }

    public void setNotified(boolean isNotified) {
        this.isNotified = isNotified;
    }
}