package ru.stmlabs.ticketservice.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    Long findIDByLogin(String login);

    String findLoginUser(String login);

}
