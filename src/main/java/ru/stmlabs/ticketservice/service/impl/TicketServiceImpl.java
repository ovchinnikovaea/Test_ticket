package ru.stmlabs.ticketservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
//import ru.stmlabs.ticketservice.dto.CreateOrUpdateTicketDto;
import ru.stmlabs.ticketservice.dto.CreateOrUpdateTicketDto;
import ru.stmlabs.ticketservice.dto.TicketDto;
import ru.stmlabs.ticketservice.entity.Route;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.entity.User;
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
        int page = pageNumber;
        int size = pageSize;

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
    public TicketDto buyTicket(int id, Long userId) {
        // Проверяем, существует ли билет
        String sqlCheckTicket = "SELECT status FROM ticket WHERE id = ?";
        String status = jdbcTemplate.queryForObject(sqlCheckTicket, new Object[]{id}, String.class);

        if (status == null) {
            throw new IllegalArgumentException("Ticket not found");
        }

        if ("sold".equalsIgnoreCase(status)) {
            throw new IllegalStateException("Ticket is already sold");
        }

        // Получаем информацию о билете
        String sqlGetTicket =
                "SELECT t.id, t.seat, t.price, t.status, t.dataTime, r.departure, r.arrival, r.carrier " +
                        "FROM ticket t " +
                        "JOIN route r ON t.route_id = r.id " +
                        "WHERE t.id = ?";
        Ticket ticket = jdbcTemplate.queryForObject(sqlGetTicket, new Object[]{id}, new RowMapper<Ticket>() {
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

        // Обновляем статус билета
        String sqlUpdateStatus = "UPDATE ticket SET status = 'sold', user_id = ? WHERE id = ?";
        jdbcTemplate.update(sqlUpdateStatus, userId, id);

        return ticketMapper.ticketToTicketDto(ticket);

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
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                Route route = new Route();
                route.setDeparture(rs.getString("departure"));
                route.setArrival(rs.getString("arrival"));
                route.setCarrier(rs.getString("carrier"));
                route.setDuration(rs.getInt("duration"));
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

//    @Override
//    public TicketDto addTicket(CreateOrUpdateTicketDto createOrUpdateTicketDto, Long userId) {
//        Ticket ticket = ticketMapper.createOrUpdateTicketDTOToTicket(createOrUpdateTicketDto);
//
//
//        String sqlInsertTicket = "INSERT INTO ticket (route_id, date_time, seat, price, status, user_id) VALUES (?, ?, ?, ?, ?, ?)";
//        try {
//            jdbcTemplate.update(sqlInsertTicket,
//                    ticket.getRoute().getId(),
//                    ticket.getDateTime(),
//                    ticket.getSeat(),
//                    ticket.getPrice(),
//                    ticket.getStatus(),
//                    ticket.getUser().getId());;
//        } catch (DataAccessException e) {
//            throw new RuntimeException("Error inserting ticket into the database", e);
//        }
//        return ticketMapper.ticketToTicketDto(ticket);
//    }
}