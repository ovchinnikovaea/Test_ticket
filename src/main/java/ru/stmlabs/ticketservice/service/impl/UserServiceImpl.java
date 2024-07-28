package ru.stmlabs.ticketservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.stmlabs.ticketservice.dto.RegisterDto;
import ru.stmlabs.ticketservice.entity.User;
import ru.stmlabs.ticketservice.exception.LoginAlreadyExistsException;
import ru.stmlabs.ticketservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public RegisterDto registerUser(RegisterDto body) throws LoginAlreadyExistsException {
        User user = new User();
        user.setLogin(body.getUsername());
        user.setPassword(body.getPassword());
        user.setFullName(body.getFullName());

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
            jdbcTemplate.update(sqlInsertUser, body.getUsername(), body.getPassword(), body.getFullName());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting user into the database", e);
        }
        return body;
    }
}
