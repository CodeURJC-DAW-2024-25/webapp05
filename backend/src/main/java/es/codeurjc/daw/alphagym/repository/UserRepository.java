package es.codeurjc.daw.alphagym.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.codeurjc.daw.alphagym.model.User;
import java.util.Optional;

public interface    UserRepository extends JpaRepository<User, Long> {

    Optional findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findByName(String name);
    boolean existsByEmail(String email);

}
