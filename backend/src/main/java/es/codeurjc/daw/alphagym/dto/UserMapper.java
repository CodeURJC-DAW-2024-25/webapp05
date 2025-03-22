package es.codeurjc.daw.alphagym.dto;

import org.mapstruct.Mapper;
import es.codeurjc.daw.alphagym.model.User;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring") // Use "Spring" to be detected by Spring Boot
public interface UserMapper {

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

    default List<UserDTO> toUserDTOs(List<User> users) {
        return users.stream().map(this::toUserDTO).collect(Collectors.toList());
    }
    
}