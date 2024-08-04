package ru.stmlabs.ticketservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.stmlabs.ticketservice.entity.Route;

import java.time.LocalDateTime;

public class TicketDto {

    @JsonProperty("route")
    private Integer route;

    @JsonProperty("dateTime")
    private LocalDateTime dateTime;

    @JsonProperty("price")
    private Integer price;

}
