package ru.stmlabs.ticketservice.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.stmlabs.ticketservice.dto.RegisterDto;
import ru.stmlabs.ticketservice.entity.User;
import ru.stmlabs.ticketservice.exception.LoginAlreadyExistsException;
import ru.stmlabs.ticketservice.mapper.UserMapper;
import ru.stmlabs.ticketservice.repository.UserRepository;
import ru.stmlabs.ticketservice.security.JwtUtils;
import ru.stmlabs.ticketservice.service.UserService;

import static ru.stmlabs.ticketservice.repository.constants.SQLConstants.SQL_ADD_USER;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final JdbcTemplate jdbcTemplate;

    private final JwtUtils jwtUtils;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public UserServiceImpl(JdbcTemplate jdbcTemplate, JwtUtils jwtUtils, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public RegisterDto registerUser(RegisterDto body) throws LoginAlreadyExistsException {
        log.info("Received params: " + body);
        User user = new User();
        user.setLogin(body.getUsername());
        user.setFullName(body.getFullName());
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        user.setRole(body.getRole());

        try {
            String existingLogin = userRepository.findLoginUser(body.getUsername());
            log.error("Existing login is " + existingLogin);
            if (existingLogin != null && existingLogin.equals(body.getUsername())) {
                throw new LoginAlreadyExistsException("User with login " + body.getUsername() + " already exists.");
            }
        } catch (EmptyResultDataAccessException e) {
        }

        try {
            jdbcTemplate.update(SQL_ADD_USER, user.getLogin(), user.getPassword(), user.getFullName());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting user into the database", e);
        }
        log.info("User " + body + " saved in database");
        return body;
    }

    @Override
    public Long getUserId(HttpServletRequest request) {
        String authorizationHeader = jwtUtils.getAuthorizationHeader(request);
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            log.error("Authorization header is missing or empty");
            return null;
        }
        String userName = jwtUtils.getUserNameFromJwtToken(authorizationHeader);
        try {
            return userRepository.findIDByLogin(userName);
        } catch (EmptyResultDataAccessException e) {
            log.error("User not found with username: " + userName);
            return null;
        } catch (Exception e) {
            log.error("An error occurred while retrieving user ID for username: " + userName, e);
            return null;
        }
    }
}
