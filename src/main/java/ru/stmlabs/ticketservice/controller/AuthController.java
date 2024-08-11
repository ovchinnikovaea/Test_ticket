package ru.stmlabs.ticketservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.stmlabs.ticketservice.dto.LoginDto;
import ru.stmlabs.ticketservice.dto.RegisterDto;
import ru.stmlabs.ticketservice.service.AuthService;
import ru.stmlabs.ticketservice.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterDto> register(@Valid @RequestBody RegisterDto body) {
        return new ResponseEntity<>(userService.registerUser(body), HttpStatus.CREATED);
    }

    @Operation(summary = "Авторизация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDTO) {
        String jwtToken = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return ResponseEntity.ok(jwtToken);
    }
}
