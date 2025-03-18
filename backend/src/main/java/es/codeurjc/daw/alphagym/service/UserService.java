package es.codeurjc.daw.alphagym.service;

import es.codeurjc.daw.alphagym.dto.UserDTO;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.UserRepository;
import es.codeurjc.daw.alphagym.security.LoginRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import es.codeurjc.daw.alphagym.dto.UserMapper;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
	private UserMapper mapper;

    private UserDTO toUserDTO(User user) {
        return mapper.toUserDTO(user);
    }

    private User toUser(UserDTO userDTO) {
        return mapper.toUser(userDTO);
    }
    
    public ResponseEntity<Object> login(LoginRequest loginRequest) {
        
        Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok().build();
    }

    public User createUser(String name, String email, String pass, String... roles) throws SQLException, IOException {
        User user = new User();
    
        user.setName(name);
        user.setEmail(email);
        user.setEncodedPassword(passwordEncoder.encode(pass));
        user.setRoles(List.of(roles));

        ClassPathResource imgFileDefault = new ClassPathResource("static/images/emptyImage.png");
        byte[] imageBytesDefault = Files.readAllBytes(imgFileDefault.getFile().toPath());
        Blob imageBlobDefault = new SerialBlob(imageBytesDefault);
        user.setImgUser(imageBlobDefault);
        user.setImage(true);
    
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

    public UserDTO getUser(String name) {
        return mapper.toUserDTO(userRepository.findByName(name).orElseThrow());
    }
    
    public UserDTO updateUser(User User) {
        return mapper.toUserDTO(userRepository.save(User));
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = toUser(userDTO);
        userRepository.save(user);
        return toUserDTO(user);
    }

    public UserDTO replaceUser(Long id, UserDTO updateUserDTO) {

        if (userRepository.existsById(id)) {

            User updateUser = toUser(updateUserDTO);
            updateUser.setId(id);

            userRepository.save(updateUser);

            return toUserDTO(updateUser);
        } else {
            throw new NoSuchElementException(); 
        }
    }

}
