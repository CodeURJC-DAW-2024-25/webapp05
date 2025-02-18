package es.codeurjc.daw.aplhaGym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class User { 
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO) 

    private long id; 
    private String name; 
    private String email;
    private String address;
    private String password;

    // Constructor necesario para la carga desde BBDD 
    protected User() {} 
    
    public User(String name, String email, String address, String password) {
        this.name = name; 
        this.email = email; 
        this.address = address;
        this.password = password;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
      }
      
    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setPassword(String password) {
      this.password = password;
    }
    
} 