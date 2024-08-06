package ru.stmlabs.ticketservice.entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@EqualsAndHashCode
public class Ticket {
    private Integer id;
    private Integer routeId;
    private LocalDateTime dateTime;
    private String seat;
    private int price;
    private String status;
    private Integer userId;
}

