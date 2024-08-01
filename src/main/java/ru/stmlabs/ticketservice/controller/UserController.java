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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stmlabs.ticketservice.dto.UserDto;
import ru.stmlabs.ticketservice.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(Authentication authentication) {
        String username = authentication.getName();
        logger.info("Fetching user details for: {}", username);
        UserDto userDto = userService.getUserDto(username);
        if (userDto == null) {
            logger.error("User not found: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.debug("User details found: {}", userDto);
        return ResponseEntity.ok(userDto);
    }
}
