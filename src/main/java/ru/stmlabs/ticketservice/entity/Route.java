package ru.stmlabs.ticketservice.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String departure;
    private String arrival;
    private String carrier;
    private int duration;
    @OneToMany(mappedBy = "route")
    private List<Ticket> tickets;
}

