package ru.stmlabs.ticketservice.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.exception.TicketNotFoundException;
import ru.stmlabs.ticketservice.exception.UserNotFoundException;
import ru.stmlabs.ticketservice.mapper.TicketRowMapper;
import ru.stmlabs.ticketservice.repository.TicketRepository;

import java.util.List;

import static ru.stmlabs.ticketservice.repository.constants.SQLConstants.*;

@Slf4j
@Repository
public class TicketRepositoryImpl implements TicketRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Ticket> getTicketsByUserId(Long userId) {
        try {
            return jdbcTemplate.query(SQL_GET_TICKETS_BY_USER_ID, new Object[]{userId}, new TicketRowMapper());
        } catch (EmptyResultDataAccessException e) {
            log.error("User with ID: " + userId + " not found");
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
    }

    @Override
    public String getTicketStatusById(int ticketId) {
        return jdbcTemplate.queryForObject(SQL_GET_STATUS_BY_TICKET_ID, new Object[]{ticketId}, String.class);
    }

    @Override
    public Ticket getTicketById(int ticketId) {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_TICKET_BY_TICKET_ID, new Object[]{ticketId}, new TicketRowMapper());
        } catch (EmptyResultDataAccessException e) {
            log.error("Ticket with ID: " + ticketId + " not found");
            throw new TicketNotFoundException("Ticket with ID " + ticketId + " not found.");
        }
    }

    @Override
    public Ticket updateStatusTicket(Long userId, int ticketId) {
        Ticket ticket = getTicketById(ticketId);
        log.info("Ticket with ID: " + ticketId + " is find in database");
        jdbcTemplate.update(SQL_UPDATE_TICKET_STATUS, userId, ticketId);
        log.info("Ticket with ID: " + ticketId + " purchased by user with ID: " + userId);
        return ticket;
    }

    @Override
    public List<Ticket> getTicketsByParam(String sqlTicket, List<Object> params) {
        return jdbcTemplate.query(sqlTicket, params.toArray(), new TicketRowMapper());
    }

}

