package ru.stmlabs.ticketservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import ru.stmlabs.ticketservice.dto.CreateOrUpdateTicketDto;
import ru.stmlabs.ticketservice.dto.CreateOrUpdateTicketDto;
import ru.stmlabs.ticketservice.dto.TicketDto;
import ru.stmlabs.ticketservice.dto.TicketsDto;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.entity.User;
import ru.stmlabs.ticketservice.exception.UserNotFoundException;
import ru.stmlabs.ticketservice.service.TicketService;
import ru.stmlabs.ticketservice.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;
    private final UserController userController;


    public TicketController(TicketService ticketService, UserService userService, UserController userController) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.userController = userController;

    }

    @Operation(summary = "Получение списка всех билетов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets(@RequestHeader("Authorization") String authorizationHeader) {
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
    public ResponseEntity<TicketDto> buyTicket(@PathVariable int id, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            Long userId = userController.getUser(authorizationHeader);
            TicketDto ticket = ticketService.buyTicket(id, userId);
            if (ticket == null) {
                return ResponseEntity.notFound().build(); // Билет не найден
            }
            return ResponseEntity.ok(ticket); // Возвращаем найденный билет
        } catch (UserNotFoundException e) {
            // Обрабатываем исключение, если пользователь не найден
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Возвращаем статус 401
        } catch (Exception e) {
            // Обрабатываем любые другие исключения
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Возвращаем статус 500
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
            Long userId = userController.getUser(authorizationHeader);
            List<Ticket> tickets = ticketService.getTicketsMe(userId);
            return ResponseEntity.ok(tickets); // Возвращаем найденный билет

        } catch (UserNotFoundException e) {
            // Обрабатываем исключение, если пользователь не найден
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Возвращаем статус 401
        } catch (Exception e) {
            // Обрабатываем любые другие исключения
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Возвращаем статус 500
        }

    }
//    @Operation(summary = "Добавление билета")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Created"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized")
//    })
//    @PostMapping("/add")
//    public ResponseEntity<TicketDto> addAd(@RequestBody CreateOrUpdateTicketDto createOrUpdateTicketDto,
//                                      @RequestHeader("Authorization") String authorizationHeader) {
//        try {
//            Long userId = userController.getUser(authorizationHeader);
//            TicketDto ticket = ticketService.addTicket(createOrUpdateTicketDto, userId);
//            return ResponseEntity.ok(ticket); // Возвращаем найденный билет
//
//        } catch (UserNotFoundException e) {
//            // Обрабатываем исключение, если пользователь не найден
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Возвращаем статус 401
//        } catch (Exception e) {
//            // Обрабатываем любые другие исключения
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Возвращаем статус 500
//        }
//    }
}
