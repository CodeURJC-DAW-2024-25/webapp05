package es.codeurjc.daw.alphagym.dto;

import org.mapstruct.Mapper;
//import org.springframework.stereotype.Component;
import es.codeurjc.daw.alphagym.model.User;


@Mapper(componentModel = "spring") // Use "Spring" to be detected by Spring Boot
public interface UserMapper {

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);
}
/* 
@Component
@Mapper(componentModel = "User")
public interface UserMapper {

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

}
*/