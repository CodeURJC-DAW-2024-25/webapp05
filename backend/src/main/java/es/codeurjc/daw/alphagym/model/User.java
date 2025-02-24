package es.codeurjc.daw.alphagym.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity 
public class User {

    @Id ()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name; 
    private String email;
    private String password;

    @OneToMany(cascade=CascadeType.ALL)
    private List<TrainingComment> trainingComments;

    @OneToMany(cascade=CascadeType.ALL)
    private List<NutritionComment> nutritionComments;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Training> trainings;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Nutrition> nutritions;
    
    public User(String name, String email, String password) {
        this.name = name; 
        this.email = email; 
        this.password = password;
    }

    // Constructor necesario para la carga desde BBDD
    public User() {}

    // Getters

    public List<Training> getTrainings() {
        return trainings;
    }

    public List<Nutrition> getNutritions() {
        return nutritions;
    }
    
    public List<TrainingComment> getTrainingComments() {
        return trainingComments;
    }

    public List<NutritionComment> getNutritionComments() {
        return nutritionComments;
    }

    public Long getId() { return id;}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
      }
      
    // Setters

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public void setNutritions(List<Nutrition> nutritions) {
        this.nutritions = nutritions;
    }

    public void setTrainingComments(List<TrainingComment> trainingComments) {
        this.trainingComments = trainingComments;
    }

    public void setNutritionComments(List<NutritionComment> nutritionComments) {
        this.nutritionComments = nutritionComments;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
      this.password = password;
    }
    
} 