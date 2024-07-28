package ru.stmlabs.ticketservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.stmlabs.ticketservice.entity.Route;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.exception.LoginAlreadyExistsException;
import ru.stmlabs.ticketservice.mapper.TicketMapper;
import ru.stmlabs.ticketservice.service.TicketService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private final JdbcTemplate jdbcTemplate;
    private final TicketMapper ticketMapper;

    @Autowired
    public TicketServiceImpl(JdbcTemplate jdbcTemplate, TicketMapper ticketMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.ticketMapper = ticketMapper;
    }


    @Override
    public List<Ticket> getAllTickets() {
        String sqlTickets = "SELECT t.*, r.departure, r.arrival, r.carrier, r.duration " +
                "FROM ticket t " +
                "JOIN route r ON t.route_id = r.id";
        List<Ticket> tickets = jdbcTemplate.query(sqlTickets, new RowMapper<Ticket>() {
            @Override
            public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                Route route = new Route();
                route.setDeparture(rs.getString("departure"));
                route.setArrival(rs.getString("arrival"));
                route.setCarrier(rs.getString("carrier"));
                ticket.setRoute(route);
                ticket.setDateTime(rs.getTimestamp("dataTime").toLocalDateTime());
                ticket.setSeat(rs.getString("seat"));
                ticket.setPrice(rs.getInt("price"));
                ticket.setStatus(rs.getString("status"));
                return ticket;
            }
        });
        return tickets;
    }

    @Override
    public List<Ticket> getAllTicketsByParam(LocalDateTime dateTime, String departure, String arrival, String carrier, int pageNumber, int pageSize) {
        // SQL-запрос с поддержкой фильтрации по критериям
        StringBuilder sqlBuilder = new StringBuilder("SELECT t.*, r.departure, r.arrival, r.carrier FROM ticket t JOIN route r ON t.route_id = r.id WHERE t.status = 'available'");

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
// Пагинация
        int page = pageNumber; // номер страницы (0-based)
        int size = pageSize; // размер страницы


        sqlBuilder.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);
        String sqlTickets = sqlBuilder.toString();
        List<Ticket> tickets = jdbcTemplate.query(sqlTickets, params.toArray(), new RowMapper<Ticket>() {
            @Override
            public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                Route route = new Route();
                route.setDeparture(rs.getString("departure"));
                route.setArrival(rs.getString("arrival"));
                route.setCarrier(rs.getString("carrier"));
                ticket.setRoute(route);
                ticket.setDateTime(rs.getTimestamp("dataTime").toLocalDateTime());
                ticket.setSeat(rs.getString("seat"));
                ticket.setPrice(rs.getInt("price"));
                ticket.setStatus(rs.getString("status"));
                return ticket;
            }
        });

        return tickets;
    }

    @Override
    public Ticket buyTicket(int id) {
        String sqlStatus = "SELECT status FROM ticket WHERE id = ?";
        String status = jdbcTemplate.queryForObject(sqlStatus, new Object[]{id}, String.class);

        if ("sold".equalsIgnoreCase(status)) {
            throw new IllegalStateException("Ticket is already sold");
        }

        String sqlTickets = "SELECT * FROM ticket WHERE id = ?";
        Ticket ticket = jdbcTemplate.queryForObject(sqlTickets, new Object[]{id}, new RowMapper<Ticket>() {
            @Override
            public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                Route route = new Route();
                route.setDeparture(rs.getString("departure"));
                route.setArrival(rs.getString("arrival"));
                route.setCarrier(rs.getString("carrier"));
                ticket.setRoute(route);
                ticket.setDateTime(rs.getTimestamp("dataTime").toLocalDateTime());
                ticket.setSeat(rs.getString("seat"));
                ticket.setPrice(rs.getInt("price"));
                ticket.setStatus(rs.getString("status"));
                return ticket;
            }
        });
        String updateStatusSql = "UPDATE ticket SET status = 'sold' WHERE id = ?";
        jdbcTemplate.update(updateStatusSql, id);
        return ticket;
    }
}