package com.example.webapp05.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity 
public class User { 
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private long id; 
    
    private String name; 
    private String email;
    private String address;

    // Constructor necesario para la carga desde BBDD 
    protected User() {} 
    
    public User(String name, String email, String address) {
        this.name = name; 
        this.email = email; 
        this.address = address;
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
    
}