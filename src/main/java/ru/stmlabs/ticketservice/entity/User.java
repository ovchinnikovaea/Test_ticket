package ru.stmlabs.ticketservice.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.stmlabs.ticketservice.enums.ERole;

@Getter
@Setter
@EqualsAndHashCode
public class User {
    private Long id;
    private String login;
    private String password;
    private String fullName;
    private ERole role;

    public User() {
    }

    public User(long id, String login, String password, String fullName) {
    }
}

