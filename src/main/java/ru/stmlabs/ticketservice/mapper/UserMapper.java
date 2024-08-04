package ru.stmlabs.ticketservice.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.stmlabs.ticketservice.dto.UserDto;
import ru.stmlabs.ticketservice.entity.User;
import ru.stmlabs.ticketservice.enums.ERole;

@Component
@Mapper(componentModel = "spring", imports = {ERole.class})
public abstract class UserMapper {

    public abstract UserDto userToUserDto(User user);


    public abstract User userDtoToUser(UserDto userDto);

}
