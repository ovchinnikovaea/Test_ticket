package ru.stmlabs.ticketservice.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import ru.stmlabs.ticketservice.enums.ERole;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    private String fullName;
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "role")
    private Role Role;
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    public User() {

    }

    public User(long id, String login, String password, String fullName) {
    }
}

