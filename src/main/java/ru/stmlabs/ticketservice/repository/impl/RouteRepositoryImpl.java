package ru.stmlabs.ticketservice.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.stmlabs.ticketservice.entity.Route;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.exception.TicketNotFoundException;
import ru.stmlabs.ticketservice.mapper.RouteRowMapper;
import ru.stmlabs.ticketservice.mapper.TicketRowMapper;
import ru.stmlabs.ticketservice.repository.RouteRepository;

import static ru.stmlabs.ticketservice.repository.constants.SQLConstants.SQL_GET_ROUTE_BY_ID;
import static ru.stmlabs.ticketservice.repository.constants.SQLConstants.SQL_GET_TICKET_BY_TICKET_ID;

@Repository
public class RouteRepositoryImpl implements RouteRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Route getRouteById(int routeId) {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_ROUTE_BY_ID, new Object[]{routeId}, new RouteRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new TicketNotFoundException("Route with ID " + routeId + " not found.");
        }
    }
}
