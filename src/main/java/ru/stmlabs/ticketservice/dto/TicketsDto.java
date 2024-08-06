package ru.stmlabs.ticketservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class TicketsDto {
    @JsonProperty("count")
    private Integer count;

    @JsonProperty("results")
    private List<TicketDto> results;
}
