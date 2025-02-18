package com.example.webapp05.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name); 
    List<User> findByEmail(String email);
    List<User> findByAddress(String address);
    List<User> findByNameAndEmail(String name, String email);
    List<User> findByNameAndAddress(String name, String address);   
}
