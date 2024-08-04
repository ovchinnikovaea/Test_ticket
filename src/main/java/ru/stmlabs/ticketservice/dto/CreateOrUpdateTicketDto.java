package ru.stmlabs.ticketservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.Id;
import ru.stmlabs.ticketservice.entity.Route;
import ru.stmlabs.ticketservice.entity.User;

import java.time.LocalDateTime;

public class CreateOrUpdateTicketDto {
    @JsonProperty("route")
    private Route route;
    @JsonProperty("dateTime")
    private LocalDateTime dateTime;
    @JsonProperty("seat")
    private String seat;
    @JsonProperty("price")
    private int price;

}
