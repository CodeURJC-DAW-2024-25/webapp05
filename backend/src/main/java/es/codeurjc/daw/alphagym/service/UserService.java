package es.codeurjc.daw.alphagym.service;

import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        userRepository.save(user);
        return user;
    }

    public User getUser(Long id){
        @SuppressWarnings("unchecked")
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean exist(Long id) {
        return userRepository.existsById(id);
    }

    public boolean existEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }
}
