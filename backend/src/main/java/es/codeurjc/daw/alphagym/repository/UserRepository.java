package es.codeurjc.daw.alphagym.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.codeurjc.daw.alphagym.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name); 
    List<User> findByEmail(String email);
    List<User> findByAddress(String address);
    List<User> findByNameAndEmail(String name, String email);
    List<User> findByNameAndAddress(String name, String address);  

}
