package es.codeurjc.daw.alphagym.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String trainingName;
    private int duration;
    private String intensity;
    private String description;
    @Column(length = 1500000)
    private String image;
    private String goal;

    @OneToMany(cascade=CascadeType.ALL)
    private List<TrainingComment> comments;

    public Training(String trainingName, int duration, String intensity, String description, String image, String goal) {
        this.trainingName = trainingName;
        this.duration = duration;
        this.intensity = intensity;
        this.description = description;
        this.image = image;
        this.goal = goal;
    }

    public Training() {
    }

    // Getters

    public List<TrainingComment> getComments() {
        return comments;
    }

    public long getId() { return id;}

    public int getDuration() { return duration;}
    public String getTrainingName() { return trainingName;}

    public String getIntensity() { return intensity;}
    public String getDescription() { return description;}

    public String getGoal() { return goal;}
    public String getImage() { return image;}

    // Setters

    public void setTrainingComments(List<TrainingComment> comments) {
        this.comments = comments;
    }
    public void setId(long id) {this.id = id;}
    public void setDuration(int duration) { this.duration = duration;}
    public void setTrainingName(String name) { this.trainingName = name; }
    public void setIntensity(String intensity) { this.intensity = intensity;}
    public void setDescription(String description) {
        this.description = description;
    }
    public void setGoal(String goal) { this.goal = goal;}
    public void setImage(String image) {
        this.image = image;
    }

}
