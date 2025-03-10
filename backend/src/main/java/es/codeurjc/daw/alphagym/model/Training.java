package es.codeurjc.daw.alphagym.model;

import es.codeurjc.daw.alphagym.model.User;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private int duration;
    private String intensity;
    private String description;
    @Column(length = 1500000)
    private String image;
    private String goal;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TrainingComment> comments;

    //@OnetoMany() mappedBy = "trainings" //tengo que mirar la sintaxis 
    //private List<User> users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    public Training(String name, String intensity,int duration,  String goal, String description) {
        this.name = name;
        this.intensity = intensity;
        this.duration = duration;
        this.goal = goal;
        this.description = description;
        this.image = "/images/emptyImage.png";

    }

    public Training() {
    }

    // Getters

    public List<TrainingComment> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    public long getId() { return id;}

    public int getDuration() { return duration;}
    public String getName() { return name;}

    public String getIntensity() { return intensity;}
    public String getDescription() { return description;}

    public String getGoal() { return goal;}
    public String getImage() { return image;}

    public User getUser() { return user; }

    // Setters

    public void setTrainingComments(List<TrainingComment> comments) {
        this.comments = comments;
    }
    public void setId(long id) {this.id = id;}
    public void setDuration(int duration) { this.duration = duration;}
    public void setName(String name) { this.name = name; }
    public void setIntensity(String intensity) { this.intensity = intensity;}
    public void setDescription(String description) {
        this.description = description;
    }
    public void setGoal(String goal) { this.goal = goal;}
    public void setImage(String image) {
        this.image = image;
    }
    public void setUser(User user) {this.user = user;}
}
