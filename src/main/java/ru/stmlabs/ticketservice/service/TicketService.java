package ru.stmlabs.ticketservice.service;

//import ru.stmlabs.ticketservice.dto.CreateOrUpdateTicketDto;
import ru.stmlabs.ticketservice.entity.Ticket;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketService {

    List<Ticket> getAllTicketsByParam(
            LocalDateTime dateTime,
            String departure,
            String arrival,
            String carrier,
            int pageNumber,
            int pageSize);

    Ticket buyTicket(int id, Long userId);

    List<Ticket> getTicketsMe(Long userId);

}
