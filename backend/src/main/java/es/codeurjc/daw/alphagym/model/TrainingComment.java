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
    private String name;
    private boolean isNotified = false;
    private Long trainingId;
    
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "training_id")

    protected TrainingComment() {
    }

    public TrainingComment(String description, String name, Long trainingId) {
        this.description = description;
        this.name = name;
        this.trainingId = trainingId;
    }

    public TrainingComment(String description, String name) {
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

    public String getdescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public boolean getIsNotified() {
        return isNotified;
    }

    public Long getTrainingId() { 
        return trainingId; 
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setdescription(String description) {
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

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }
}