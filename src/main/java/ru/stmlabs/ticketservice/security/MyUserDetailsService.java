package ru.stmlabs.ticketservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.stmlabs.ticketservice.entity.User;
import ru.stmlabs.ticketservice.exception.LoginNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Transactional
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        String query = "SELECT * FROM app_user WHERE login = ?";
        User user = jdbcTemplate.queryForObject(query, new Object[]{login}, this::mapRowToUser);
        if (user == null) {

            throw new LoginNotFoundException(login);
        }
        return new MyUserPrincipal(user);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("fullName"));
        return user;
    }
}
