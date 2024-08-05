package ru.stmlabs.ticketservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.stmlabs.ticketservice.dto.RegisterDto;
import ru.stmlabs.ticketservice.dto.UserDto;
import ru.stmlabs.ticketservice.entity.User;
import ru.stmlabs.ticketservice.exception.LoginAlreadyExistsException;
import ru.stmlabs.ticketservice.exception.UserNotFoundException;
import ru.stmlabs.ticketservice.mapper.UserMapper;
import ru.stmlabs.ticketservice.security.JwtUtils;
import ru.stmlabs.ticketservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;

    @Override
    public RegisterDto registerUser(RegisterDto body) throws LoginAlreadyExistsException {

        User user = new User();
        user.setLogin(body.getUsername());
        user.setFullName(body.getFullName());
        user.setRole(body.getRole());
        user.setPassword(passwordEncoder.encode(body.getPassword()));

        String sqlCheckUser = "SELECT login FROM app_user WHERE login = ?";
        try {
            String existingLogin = jdbcTemplate.queryForObject(sqlCheckUser, new Object[]{body.getUsername()}, String.class);
            if (existingLogin != null) {
                throw new LoginAlreadyExistsException("User with login " + body.getUsername() + " already exists.");
            }
        } catch (EmptyResultDataAccessException e) {
        }

        String sqlInsertUser = "INSERT INTO app_user (login, password, fullName) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sqlInsertUser, user.getLogin(), user.getPassword(), user.getFullName());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting user into the database", e);
        }
        return body;
    }

    private static final String SQL_FIND_USER_BY_USERNAME = "SELECT * FROM app_user WHERE login = ?";

    @Override
    public UserDto getUserDto(String userName) {
        try {
            User user = getUser(userName);
            return userMapper.userToUserDto(user);
        } catch (UserNotFoundException e) {
            return null;
        }
    }


    @Override
    public User getUser(String username) throws UserNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_USER_BY_USERNAME, new Object[]{username}, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("fullName"));
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User not found with username: " + username);
        }
    }

    @Override
    public Long getUserId(String username) throws UserNotFoundException {
        try {
            String sqlCheckUser = "SELECT id FROM app_user WHERE login = ?";
            Long userId = jdbcTemplate.queryForObject(sqlCheckUser, new Object[]{username}, Long.class);
            return userId;
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User not found with username: " + username);
        }
    }
}
