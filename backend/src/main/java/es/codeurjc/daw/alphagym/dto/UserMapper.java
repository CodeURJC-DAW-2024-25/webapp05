package es.codeurjc.daw.alphagym.dto;

import org.mapstruct.Mapper;
import es.codeurjc.daw.alphagym.model.User;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "User")
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getRoles(),
            user.getEmail()
        );
    }
}