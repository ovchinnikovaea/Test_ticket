package ru.stmlabs.ticketservice.repository;

import org.springframework.stereotype.Repository;
import ru.stmlabs.ticketservice.entity.Ticket;

import java.util.List;

@Repository
public interface TicketRepository{
    List<Ticket> getTicketsByUserId(Long userId);

    String getTicketStatusById(int ticketId);

    Ticket getTicketById(int ticketId);

    Ticket updateStatusTicket(Long userId, int ticketId);

    List<Ticket> getTicketsByParam(String sqlTicket, List<Object> params);
}
