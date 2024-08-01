package ru.stmlabs.ticketservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class TicketDto {

    @JsonProperty("route")
    private String route;

    @JsonProperty("dateTime")
    private LocalDateTime dateTime;

    @JsonProperty("price")
    private Integer price;

}
