package ru.stmlabs.ticketservice.service;

import ru.stmlabs.ticketservice.dto.RegisterDto;
import ru.stmlabs.ticketservice.dto.UserDto;
import ru.stmlabs.ticketservice.entity.User;
import ru.stmlabs.ticketservice.exception.LoginAlreadyExistsException;
import ru.stmlabs.ticketservice.exception.UserNotFoundException;

public interface UserService {
    RegisterDto registerUser(RegisterDto body) throws LoginAlreadyExistsException;

    UserDto getUserDto(String userName);

    User getUser(String username) throws UserNotFoundException;
}
