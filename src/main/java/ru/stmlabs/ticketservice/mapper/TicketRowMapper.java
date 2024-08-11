package ru.stmlabs.ticketservice.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.stmlabs.ticketservice.entity.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketRowMapper implements RowMapper<Ticket> {
    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        ticket.setRouteId(rs.getInt("route_id"));
        ticket.setDateTime(rs.getTimestamp("dataTime").toLocalDateTime());
        ticket.setSeat(rs.getString("seat"));
        ticket.setPrice(rs.getInt("price"));
        ticket.setStatus(rs.getString("status"));
        return ticket;
    }
}


