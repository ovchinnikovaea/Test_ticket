package ru.stmlabs.ticketservice.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import ru.stmlabs.ticketservice.entity.Route;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.exception.TicketNotFoundException;
import ru.stmlabs.ticketservice.mapper.TicketRowMapper;

import static ru.stmlabs.ticketservice.repository.constants.SQLConstants.SQL_GET_TICKET_BY_TICKET_ID;
@Repository
public interface RouteRepository {
Route getRouteById(int routeId);

}
