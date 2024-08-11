package ru.stmlabs.ticketservice.service;

import ru.stmlabs.ticketservice.entity.Ticket;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface TicketService {

    List<Ticket> getAllTicketsByParam(
            LocalDateTime dateTime,
            String departure,
            String arrival,
            String carrier,
            int pageNumber,
            int pageSize)
            throws SQLException;

    Ticket buyTicket(int ticketId, Long userId);

    List<Ticket> getTicketsMe(Long userId);

}
