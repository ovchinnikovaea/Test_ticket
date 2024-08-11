package ru.stmlabs.ticketservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.service.TicketService;
import ru.stmlabs.ticketservice.service.UserService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;

    @Operation(summary = "Получение билетов по параметрам")
    @SecurityRequirement(name = "Bearer Authentication")
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
            @RequestParam(defaultValue = "10") int pageSize) throws SQLException {
        List<Ticket> tickets = ticketService.getAllTicketsByParam(dateTime, departure, arrival, carrier, pageNumber, pageSize);
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Покупка билета")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @PostMapping("/{id}/buy")
    public ResponseEntity<Ticket> buyTicket(@PathVariable int id, HttpServletRequest request) {
        Long userId = userService.getUserId(request);

        Ticket ticket = ticketService.buyTicket(id, userId);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Получение купленных билетов авторизованного пользователя")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(value = "/me")
    public ResponseEntity<List<Ticket>> getTicketsMe(HttpServletRequest request) {
        Long userId = userService.getUserId(request);

        List<Ticket> tickets = ticketService.getTicketsMe(userId);
        return ResponseEntity.ok(tickets);
    }

}

