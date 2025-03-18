package es.codeurjc.daw.alphagym.dto;

import org.mapstruct.Mapper;
import es.codeurjc.daw.alphagym.model.User;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "User")
public interface UserMapper {

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

}