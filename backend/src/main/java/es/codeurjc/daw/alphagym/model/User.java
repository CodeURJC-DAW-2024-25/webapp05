package es.codeurjc.daw.alphagym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name; 
    private String email;
    private String address;
    private String password;
    
    public User(String name, String email, String address, String password) {
        this.name = name; 
        this.email = email; 
        this.address = address;
        this.password = password;
    }

    // Constructor necesario para la carga desde BBDD
    public User() {}

    // Getters
    public Long getId() { return id;}

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

    public void setId(Long id) {
        this.id = id;
    }

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