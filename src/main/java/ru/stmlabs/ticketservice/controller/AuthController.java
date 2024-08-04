package ru.stmlabs.ticketservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.stmlabs.ticketservice.dto.LoginDto;
import ru.stmlabs.ticketservice.dto.RegisterDto;
import ru.stmlabs.ticketservice.security.JwtUtils;
import ru.stmlabs.ticketservice.security.MyUserDetailsService;
import ru.stmlabs.ticketservice.service.AuthService;
import ru.stmlabs.ticketservice.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    MyUserDetailsService userDetailsService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

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
        logger.info("Login attempt for user: {}", loginDTO.getUsername());
        try {
            if (loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
                logger.warn("Username or password is missing for login attempt");
                return ResponseEntity.badRequest().body("Username or password is missing");
            }

            // Загрузка деталей пользователя
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
            logger.info("User details loaded for user: {}", loginDTO.getUsername());

            // Проверка пароля
            if (!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
                logger.warn("Password mismatch for user: {}", loginDTO.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }

            // Если пароль верен, аутентифицируем пользователя
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("User authenticated successfully: {}", loginDTO.getUsername());

            // Генерация JWT токена
            String jwtToken = jwtUtils.generateJwtToken(userDetails);
            logger.info("JWT token generated for user: {}", loginDTO.getUsername());
            return ResponseEntity.ok(jwtToken);
        } catch (Exception e) {
            logger.error("Authentication failed for user {}: {}", loginDTO.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}
