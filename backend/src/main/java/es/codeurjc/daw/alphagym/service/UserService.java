package es.codeurjc.daw.alphagym.service;

import es.codeurjc.daw.alphagym.dto.UserDTO;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.UserRepository;
import es.codeurjc.daw.alphagym.security.LoginRequest;
import jakarta.annotation.Resource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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
    
    public UserDTO updateUser(User user) {
        return mapper.toUserDTO(userRepository.save(user));
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = toUser(userDTO);
        userRepository.save(user);
        return toUserDTO(user);
    }

    public UserDTO replaceUser(Long id, UserDTO updatedUserDTO) throws SQLException {

        User oldUser = userRepository.findById(id).orElseThrow();
        User updatedUser = toUser(updatedUserDTO);
        updatedUser.setId(id);

        if (oldUser.getImgUser() != null) {

            //Set the image in the updated post 
            updatedUser.setImgUser(BlobProxy.generateProxy( 
                oldUser.getImgUser().getBinaryStream(), 
                oldUser.getImgUser().length())); 
            
                updatedUser.setImgUser(oldUser.getImgUser());  
        }

        userRepository.save(updatedUser);
        return toUserDTO(updatedUser);

    }

    public void createUserImage(long id, URI location, InputStream inputStream, long size) {
        
        User user = userRepository.findById(id).orElseThrow();
        
        user.setImgUserPath(location.toString()); //convert URI to String
        user.setImgUser(BlobProxy.generateProxy(inputStream, size)); //convert InputStream to Blob

        userRepository.save(user);

   }

    public InputStreamResource getUserImage(long id) throws SQLException{

        User user = userRepository.findById(id).orElseThrow();

        if(user.getImgUser() != null) {
            return new InputStreamResource(user.getImgUser().getBinaryStream());
        } else {
            throw new NoSuchElementException();
        }
    }

    public void replaceUserImage(long id, InputStream inputStream, long size) {
            
            User user = userRepository.findById(id).orElseThrow();

            if(user.getImgUser() == null){    
                throw new NoSuchElementException();  
            }
            
            user.setImgUser(BlobProxy.generateProxy(inputStream, size)); //convert InputStream to Blob
    
            userRepository.save(user);
    }

}
