package ru.stmlabs.ticketservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class TicketDto {

    @JsonProperty("route") // маршрут
    private String route;

    @JsonProperty("dateTime") // дата/время
    private LocalDateTime dateTime;

    @JsonProperty("price") // цена билета
    private Integer price;

}
