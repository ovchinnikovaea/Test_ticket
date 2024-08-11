package ru.stmlabs.ticketservice.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.stmlabs.ticketservice.dto.RegisterDto;
import ru.stmlabs.ticketservice.exception.LoginAlreadyExistsException;

public interface UserService {
    RegisterDto registerUser(RegisterDto body) throws LoginAlreadyExistsException;

    Long getUserId(HttpServletRequest request);
}
