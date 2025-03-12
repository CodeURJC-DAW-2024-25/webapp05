package es.codeurjc.daw.alphagym.service;

import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.UserRepository;
import es.codeurjc.daw.alphagym.security.LoginRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public ResponseEntity<Object> login(LoginRequest loginRequest) {
        
        Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok().build();
    }

    public User createUser(String name, String email, String pass, MultipartFile image, String... roles) throws IOException {
        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setEncodedPassword(passwordEncoder.encode(pass));
        user.setRoles(List.of(roles));
    
        if (!image.isEmpty()) {
            user.setImgUser(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
            user.setImage(true);
        }
    
        userRepository.save(user); 
    
        return user; 
    }

    public User createUser(String name, String email, String pass, String... roles) {
        User user = new User();
    
        user.setName(name);
        user.setEmail(email);
        user.setEncodedPassword(passwordEncoder.encode(pass));
        user.setRoles(List.of(roles));
    
        return user; 
    }

    public void updateUserName(Long userId, String newName) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(newName);
        }
    }

    public void updateUserEmail(Long userId, String newEmail) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(newEmail);
        }
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public boolean exist(Long id) {
        return userRepository.existsById(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

}
