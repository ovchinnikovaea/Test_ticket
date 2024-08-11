package ru.stmlabs.ticketservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class TicketDto {

    @JsonProperty("departure")
    private String departure;
    @JsonProperty("arrival")
    private String arrival;
    @JsonProperty("dateTime")
    private LocalDateTime dateTime;

    @JsonProperty("price")
    private Integer price;

    public TicketDto(String departure, String arrival, LocalDateTime dateTime, int price) {
    }
}