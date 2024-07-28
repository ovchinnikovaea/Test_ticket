package ru.stmlabs.ticketservice.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Carrier {
    private String name;
    private String phone;
}

