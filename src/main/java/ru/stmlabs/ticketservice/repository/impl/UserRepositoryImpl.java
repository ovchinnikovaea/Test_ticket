package ru.stmlabs.ticketservice.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.stmlabs.ticketservice.entity.User;
import ru.stmlabs.ticketservice.repository.UserRepository;

import static ru.stmlabs.ticketservice.repository.constants.SQLConstants.*;
@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Long findIDByLogin(String login) {
        return jdbcTemplate.queryForObject(SQL_FIND_ID_BY_LOGIN, new Object[]{login}, Long.class);
    }

    @Override
    public String findLoginUser(String login) {
        return jdbcTemplate.queryForObject(SQL_FIND_LOGIN_USER, new Object[]{login}, String.class);

    }
}
