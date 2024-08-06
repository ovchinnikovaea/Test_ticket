package ru.stmlabs.ticketservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
//import ru.stmlabs.ticketservice.dto.CreateOrUpdateTicketDto;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.service.TicketService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TicketServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;}

    @Override
    public List<Ticket> getAllTicketsByParam(LocalDateTime dateTime, String departure, String arrival, String carrier, int pageNumber, int pageSize) {
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT t.id, t.route_id, t.dataTime, t.seat, t.price, t.status, t.user_id, " +
                        "r.departure, r.arrival, r.carrier " +
                        "FROM ticket t " +
                        "JOIN route r ON t.route_id = r.id " +
                        "WHERE t.status = 'available'"
        );
        List<Object> params = new ArrayList<>();

        if (dateTime != null) {
            sqlBuilder.append(" AND t.dataTime = ?");
            params.add(Timestamp.valueOf(dateTime));
        }
        if (departure != null) {
            sqlBuilder.append(" AND r.departure = ?");
            params.add(departure);
        }
        if (arrival != null) {
            sqlBuilder.append(" AND r.arrival = ?");
            params.add(arrival);
        }
        if (carrier != null) {
            sqlBuilder.append(" AND r.carrier = ?");
            params.add(carrier);
        }
        int page = pageNumber;
        int size = pageSize;

        sqlBuilder.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);
        String sqlTickets = sqlBuilder.toString();
        List<Ticket> tickets = jdbcTemplate.query(sqlTickets, params.toArray(), new RowMapper<Ticket>() {
            @Override
            public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapRowToTicket(rs);
            }
        });

        return tickets;
    }

    @Override
    public Ticket buyTicket(int id, Long userId) {
        String sqlCheckTicket = "SELECT status FROM ticket WHERE id = ?";
        String status = jdbcTemplate.queryForObject(sqlCheckTicket, new Object[]{id}, String.class);

        if (status == null) {
            logger.error("Ticket with ID {} not found", id);
            throw new IllegalArgumentException("Ticket not found");
        }

        if ("sold".equalsIgnoreCase(status)) {
            logger.error("Ticket with ID {} is already sold", id);
            throw new IllegalStateException("Ticket is already sold");
        }
        String sqlGetTicket =
                "SELECT t.id, t.seat, t.price, t.status, t.dataTime, t.route_id, " +
                        "r.departure, r.arrival, r.carrier " +
                        "FROM ticket t " +
                        "JOIN route r ON t.route_id = r.id " +
                        "WHERE t.id = ?";
        Ticket ticket = jdbcTemplate.queryForObject(sqlGetTicket, new Object[]{id}, new RowMapper<Ticket>() {
            @Override
            public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapRowToTicket(rs);
            }

        });
        String sqlUpdateStatus = "UPDATE ticket SET status = 'sold', user_id = ? WHERE id = ?";
        jdbcTemplate.update(sqlUpdateStatus, userId, id);
        return ticket;

    }
    @Override
    public List<Ticket> getTicketsMe(Long userId) {
        String sqlCheckTicket = "SELECT t.*, r.departure, r.arrival, r.carrier, r.duration " +
                "FROM ticket t " +
                "JOIN route r ON t.route_id = r.id " +
                "WHERE t.user_id = ? AND t.status = 'sold'";
        ;
        List<Ticket> tickets = jdbcTemplate.query(sqlCheckTicket, new Object[]{userId}, new RowMapper<Ticket>() {
            @Override
            public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapRowToTicket(rs);
            }
        });
        return tickets;
    }
    private Ticket mapRowToTicket(ResultSet rs) throws SQLException {
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