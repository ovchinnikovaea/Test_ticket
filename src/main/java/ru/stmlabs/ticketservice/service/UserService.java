package ru.stmlabs.ticketservice.service;

import ru.stmlabs.ticketservice.dto.RegisterDto;
import ru.stmlabs.ticketservice.exception.LoginAlreadyExistsException;

public interface UserService {
    RegisterDto registerUser(RegisterDto body) throws LoginAlreadyExistsException;
}
