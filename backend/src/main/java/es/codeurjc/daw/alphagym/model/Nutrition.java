package es.codeurjc.daw.alphagym.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String image;
    private String goal;
    private int calories;

    @OneToMany(cascade=CascadeType.ALL)
    private List<NutritionComment> comments;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Nutrition(String name, int calories, String goal, String description) {
        this.name = name;
        this.calories = calories;
        this.goal = goal;
        this.description = description;
        this.image = "/images/emptyImage.png";
    }

    //Momnetaneo y experimental, puede que se quite. Para construir las dietas predefinidas
    public Nutrition(String name, int calories, String goal, String description, String Image) {
        this.name = name;
        this.calories = calories;
        this.goal = goal;
        this.description = description;
        this.image = Image;
    }

    public Nutrition() {
        }
    
        
    // Getters

    public Long getId() {
        return this.id;
    }

    public String getGoal() {
        return this.goal;
    }

    public int getCalories() {
        return this.calories;
    }

    public List<NutritionComment> getComments() {
        return comments;
    }

    public String getName() { 
        return name; }


    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public User getUser() {
        return this.user;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setComments(List<NutritionComment> comments) {
        this.comments = comments;
    }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
