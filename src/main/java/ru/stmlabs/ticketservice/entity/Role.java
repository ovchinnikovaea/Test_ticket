package ru.stmlabs.ticketservice.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import ru.stmlabs.ticketservice.enums.ERole;

@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ERole name;

    public Role(Long id, ERole name) {
        this.id = id;
        this.name = name;
    }
}
