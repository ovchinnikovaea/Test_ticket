package ru.stmlabs.ticketservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.service.TicketService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Получение списка всех билетов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @Operation(summary = "Получение списка всех билетов по параметрам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
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
    public ResponseEntity<Void> buyTicket(@PathVariable int id) {
        ticketService.buyTicket(id);
        return ResponseEntity.ok().build();
    }

}
