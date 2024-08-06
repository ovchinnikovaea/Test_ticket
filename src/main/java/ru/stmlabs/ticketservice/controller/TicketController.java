package ru.stmlabs.ticketservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.security.JwtUtils;
import ru.stmlabs.ticketservice.service.TicketService;
import ru.stmlabs.ticketservice.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
    private final TicketService ticketService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public TicketController(TicketService ticketService, UserService userService, JwtUtils jwtUtils) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @Operation(summary = "Получение билетов по параметрам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/by-param")
    public ResponseEntity<List<Ticket>> getAllTicketsByParam(
            @RequestParam(value = "dateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam(value = "departure", required = false) String departure,
            @RequestParam(value = "arrival", required = false) String arrival,
            @RequestParam(value = "carrier", required = false) String carrier,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<Ticket> tickets = ticketService.getAllTicketsByParam(dateTime, departure, arrival, carrier, pageNumber, pageSize);
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Покупка билета")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/{id}/buy")
    public ResponseEntity<Ticket> buyTicket(@PathVariable int id, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(authorizationHeader);
            Long userId = userService.getUserId(userName);
            Ticket ticket = ticketService.buyTicket(id, userId);
            if (ticket == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Получение купленных билетов авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")

    })
    @GetMapping(value = "/me")
    public ResponseEntity<List<Ticket>> getTicketsMe(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String userName = jwtUtils.getUserNameFromJwtToken(authorizationHeader);
            Long userId = userService.getUserId(userName);
            List<Ticket> tickets = ticketService.getTicketsMe(userId);
            return ResponseEntity.ok(tickets);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
