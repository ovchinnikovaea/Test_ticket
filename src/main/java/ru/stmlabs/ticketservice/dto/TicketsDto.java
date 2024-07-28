package ru.stmlabs.ticketservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class TicketsDto {
    @JsonProperty("count") // общее количество билетов
    private Integer count;

    @JsonProperty("results") // коллекция билетов
    private List<TicketDto> results;
}
