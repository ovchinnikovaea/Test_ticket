package ru.stmlabs.ticketservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stmlabs.ticketservice.dto.UserDto;
import ru.stmlabs.ticketservice.exception.UserNotFoundException;
import ru.stmlabs.ticketservice.security.JwtUtils;
import ru.stmlabs.ticketservice.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Long getUser(@RequestHeader("Authorization") String authorizationHeader) throws UserNotFoundException {
        logger.info("Received request with Authorization header: {}", authorizationHeader);

        String token = extractToken(authorizationHeader);
        logger.info("Extracted token: {}", token);

        if (token == null || !jwtUtils.validateJwtToken(token)) {
            logger.warn("Invalid or missing JWT token");
            return null;
        }

        String username = jwtUtils.getUserNameFromJwtToken(token);
        logger.info("Extracted username: {}", username);

        Long userId = userService.getUserId(username);
        if (userId == null) {
            logger.warn("User not found: {}", username);
            return null;
        }
        return userId;
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
