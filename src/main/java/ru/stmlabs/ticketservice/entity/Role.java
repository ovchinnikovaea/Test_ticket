package ru.stmlabs.ticketservice.entity;

import lombok.Getter;
import lombok.Setter;
import ru.stmlabs.ticketservice.enums.ERole;

@Getter
@Setter
public class Role {

    private Long id;
    private ERole name;

    public Role(Long id, ERole name) {
        this.id = id;
        this.name = name;
    }
}
