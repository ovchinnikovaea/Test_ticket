package ru.stmlabs.ticketservice.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Route {
    private Integer id;
    private String departure;
    private String arrival;
    private String carrier;
    private int duration;
}

