package ru.stmlabs.ticketservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.repository.TicketRepository;
import ru.stmlabs.ticketservice.service.TicketService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.stmlabs.ticketservice.repository.constants.SQLConstants.*;

@Slf4j
@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> getAllTicketsByParam(LocalDateTime dateTime, String departure, String arrival, String carrier, int pageNumber, int pageSize) throws SQLException {
        List<Object> params = createParamsForSQL(dateTime, departure, arrival, carrier);
        log.info("Received params: " + params);
        String sqlTicket = createSQLByParam(dateTime, departure, arrival, carrier);
        log.info("SQL request = " + sqlTicket);
        log.info("Successfully received tickets by specified parameters");

        List<Ticket> allTickets = ticketRepository.getTicketsByParam(sqlTicket, params);

        return getPagedTickets(allTickets, pageNumber, pageSize);
    }

    @Override
    public Ticket buyTicket(int ticketId, Long userId) {
        try {
            String status = ticketRepository.getTicketStatusById(ticketId);
            log.info("Retrieved status for ticket with ID: " + ticketId + " is " + status);

            if (status == null) {
                log.error("Status for ticket with ID: " + ticketId + " is null");
                throw new IllegalArgumentException("Ticket status is not available");
            }

            if ("sold".equalsIgnoreCase(status)) {
                log.error("Ticket with ID: " + ticketId + " is already sold");
                throw new IllegalStateException("Ticket is already sold");
            }

            log.info("Successfully bought ticket with ID: " + ticketId + " for user ID: " + userId);
            return ticketRepository.updateStatusTicket(userId, ticketId);

        } catch (Exception e) {
            log.error("Unexpected error while buying ticket with ID: " + ticketId, e);
            throw new RuntimeException("Unexpected error while processing the ticket purchase", e);
        }
    }

    @Override
    public List<Ticket> getTicketsMe(Long userId) {
        try {
            List<Ticket> tickets = ticketRepository.getTicketsByUserId(userId);
            log.info("Successfully received tickets for user with id  " + userId);
            return tickets;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while processing the ticket receipt", e);
        }
    }

    private List<Object> createParamsForSQL(LocalDateTime dateTime,
                                            String departure,
                                            String arrival,
                                            String carrier) {
        List<Object> params = new ArrayList<>();

        if (dateTime != null) {
            params.add(Timestamp.valueOf(dateTime));
        }
        if (departure != null) {
            params.add(departure);
        }
        if (arrival != null) {
            params.add(arrival);
        }
        if (carrier != null) {
            params.add(carrier);
        }

        return params;
    }

    private String createSQLByParam(LocalDateTime dateTime,
                                    String departure,
                                    String arrival,
                                    String carrier) {
        String sqlTicket = SQL_GET_AVAILABLE_TICKET;

        if (dateTime != null) {
            sqlTicket += " AND t.dataTime = ?";
        }
        if (departure != null) {
            sqlTicket += " AND r.departure = ?";
        }
        if (arrival != null) {
            sqlTicket += " AND r.arrival = ?";
        }
        if (carrier != null) {
            sqlTicket += " AND r.carrier = ?";
        }

        return sqlTicket;
    }

    public List<Ticket> getPagedTickets(List<Ticket> allTickets, int pageNumber, int pageSize) {
        int fromIndex = pageNumber* pageSize;
        if (fromIndex > allTickets.size()) {
            return Collections.emptyList();
        }
        int toIndex = Math.min(fromIndex + pageSize, allTickets.size());
        return allTickets.subList(fromIndex, toIndex);
    }
}